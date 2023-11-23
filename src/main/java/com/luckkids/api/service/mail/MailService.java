package com.luckkids.api.service.mail;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import com.luckkids.api.service.mail.request.SendPasswordServiceRequest;
import com.luckkids.api.service.mail.response.SendMailResponse;
import com.luckkids.api.service.mail.response.SendPasswordResponse;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
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
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    public SendMailResponse sendMail(SendMailServiceRequest sendMailServiceRequest) {
        String email = sendMailServiceRequest.getEmail();
        String authNum = generateCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("Luck-Maker 회원가입 인증번호"); // 메일 제목
            mimeMessageHelper.setText("인증번호: "+authNum); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            return SendMailResponse.of(authNum);
        } catch (MessagingException e) {
            throw new LuckKidsException(ErrorCode.MAIL_FAIL);
        }
    }

    @Transactional
    public SendPasswordResponse sendPassword(SendPasswordServiceRequest sendPasswordServiceRequest) {
        String email = sendPasswordServiceRequest.getEmail();
        String tempPassword = generateTempPassword();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest = UserUpdatePasswordServiceRequest.builder()
            .email(email)
            .password(tempPassword)
            .build();

        userService.updatePassword(userUpdatePasswordServiceRequest);

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("Luck-Maker 임시 비밀번호"); // 메일 제목
            mimeMessageHelper.setText("임시 비밀번호: "+tempPassword); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            return SendPasswordResponse.of(tempPassword);
        } catch (MessagingException e) {
            throw new LuckKidsException(ErrorCode.MAIL_FAIL);
        }
    }

    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        random.ints(6, 1, 10).forEach(builder::append);

        return builder.toString();
    }

    public String generateTempPassword() {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*(){}[]";
        return random.ints(15, 0, chars.length())
            .mapToObj(chars::charAt)
            .map(Object::toString)
            .collect(Collectors.joining());
    }
}
