package com.luckkids.api.service.mail;

import com.luckkids.api.component.Aes256Component;
import com.luckkids.api.controller.mail.request.SendMailRequest;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.confirmEmail.ConfirmEmailService;
import com.luckkids.api.service.confirmEmail.request.CreateConfrimEmailServiceRequest;
import com.luckkids.api.service.mail.request.SendAuthCodeServiceRequest;
import com.luckkids.api.service.mail.request.SendPasswordServiceRequest;
import com.luckkids.api.service.mail.response.SendAuthUrlResponse;
import com.luckkids.api.service.mail.response.SendPasswordResponse;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.domain.confirmEmail.ConfirmEmail;
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
    private final Aes256Component aes256Component;

    @Value("${domain.url.confirmEmail}")
    private String confirmEmailUrl;

    public SendAuthUrlResponse sendAuthUrl(SendAuthCodeServiceRequest sendAuthCodeServiceRequest) {
        String email = sendAuthCodeServiceRequest.getEmail();
        String authKey = generateRandomKey();

        String encrypt = URLEncoder.encode(aes256Component.encrypt(email+"/"+authKey), StandardCharsets.UTF_8);

        CreateConfrimEmailServiceRequest createConfrimEmailServiceRequest = CreateConfrimEmailServiceRequest.builder()
            .email(email)
            .authKey(authKey)
            .build();

        confirmEmailService.createConfirmEmail(createConfrimEmailServiceRequest);

        SendMailRequest sendMailRequest = SendMailRequest.builder()
            .email(email)
            .subject("Luck-Kids의 이메일을 인증하세요")
            .text(
                "안녕하세요.\n\n" +
                    "다음 링크를 통해 이메일 주소를 인증하세요.\n\n" +
                    confirmEmailUrl+"?key="+encrypt+ "\n\n" +
                    "이 주소로 인증을 요청하지 않았다면 이 이메일을 무시하셔도 됩니다.\n\n" +
                    "감사합니다.\n\n" +
                    "LuckKids팀"
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
            .subject("Luck-Maker 임시 비밀번호")
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
