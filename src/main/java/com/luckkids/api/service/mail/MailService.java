package com.luckkids.api.service.mail;

import com.luckkids.api.controller.mail.request.SendMailRequest;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.mail.request.SendAuthCodeServiceRequest;
import com.luckkids.api.service.mail.request.SendPasswordServiceRequest;
import com.luckkids.api.service.mail.response.SendAuthCodeResponse;
import com.luckkids.api.service.mail.response.SendPasswordResponse;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    public SendAuthCodeResponse sendAuthCode(SendAuthCodeServiceRequest sendAuthCodeServiceRequest) {
        String email = sendAuthCodeServiceRequest.getEmail();
        String authNum = generateCode();

        SendMailRequest sendMailRequest = SendMailRequest.builder()
            .email(email)
            .subject("Luck-Maker 회원가입 인증번호")
            .text("인증번호: "+authNum)
            .build();

        sendMail(sendMailRequest);

        return SendAuthCodeResponse.of(authNum);
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
            mimeMessageHelper.setTo(sendMailRequest.getText()); // 메일 수신자
            mimeMessageHelper.setSubject(sendMailRequest.getSubject()); // 메일 제목
            mimeMessageHelper.setText(sendMailRequest.getText()); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new LuckKidsException(ErrorCode.MAIL_FAIL);
        }
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        random.ints(6, 1, 10).forEach(builder::append);

        return builder.toString();
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
