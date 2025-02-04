package com.luckkids.notification.service;

import com.luckkids.notification.controller.request.SendMailRequest;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.notification.service.request.CreateConfrimEmailServiceRequest;
import com.luckkids.notification.service.request.SendAuthCodeServiceRequest;
import com.luckkids.notification.service.request.SendPasswordServiceRequest;
import com.luckkids.notification.service.response.SendAuthUrlResponse;
import com.luckkids.notification.service.response.SendPasswordResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final ConfirmEmailService confirmEmailService;
    private final SecurityService securityService;

    @Value("${domain.url.confirmEmail}")
    private String confirmEmailUrl;

    public SendAuthUrlResponse sendAuthUrl(SendAuthCodeServiceRequest sendAuthCodeServiceRequest) {
        String email = sendAuthCodeServiceRequest.getEmail();
        String authKey = generateRandomKey();

        String encrypt = URLEncoder.encode(securityService.encrypt(email+"/"+authKey), StandardCharsets.UTF_8);

        CreateConfrimEmailServiceRequest createConfrimEmailServiceRequest = CreateConfrimEmailServiceRequest.builder()
            .email(email)
            .authKey(authKey)
            .build();

        confirmEmailService.createConfirmEmail(createConfrimEmailServiceRequest);

        SendMailRequest sendMailRequest = SendMailRequest.builder()
            .email(email)
            .subject("[luckkids] 이메일 인증 안내 드려요! \uD83C\uDF40")
            .text(
                "안녕하세요, 팀 luckkids입니다.\uD83C\uDF40\n\n" +
                    "아래 링크를 클릭하여 인증을 완료해주세요!\n" +
                    "이 주소로 인증 요청하지 않으셨다면 이 메일을 무시하셔도 되어요.\n" +
                    confirmEmailUrl+"?key="+encrypt+ "\n\n" +
                    "우리는 행운아! 행운을 키우지!\n\n" +
                    "감사합니다.\n" +
                    "luckkids팀"
            )
            .build();

        sendMail(sendMailRequest);

        return SendAuthUrlResponse.of(authKey);
    }

    @Transactional
    public SendPasswordResponse sendPassword(SendPasswordServiceRequest sendPasswordServiceRequest) {
        String email = sendPasswordServiceRequest.getEmail();
        String tempPassword = generateTempPassword();

        UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest = UserUpdatePasswordServiceRequest.builder()
            .email(email)
            .password(tempPassword)
            .build();

        userService.updatePassword(userUpdatePasswordServiceRequest);

        SendMailRequest sendMailRequest = SendMailRequest.builder()
            .email(email)
            .subject("luckkids 임시 비밀번호")
            .text("임시 비밀번호: "+tempPassword)
            .build();

        sendMail(sendMailRequest);

        return SendPasswordResponse.of(email);
    }

    public void sendMail(SendMailRequest sendMailRequest){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(sendMailRequest.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject(sendMailRequest.getSubject()); // 메일 제목
            mimeMessageHelper.setText(sendMailRequest.getText()); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new LuckKidsException(ErrorCode.MAIL_FAIL);
        }
    }

    private String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[10];
        secureRandom.nextBytes(keyBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    }

    private String generateTempPassword() {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*(){}[]";
        return random.ints(15, 0, chars.length())
            .mapToObj(chars::charAt)
            .map(Object::toString)
            .collect(Collectors.joining());
    }
}
