package com.luckkids.api.service.mail;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import com.luckkids.api.service.mail.response.SendMailResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

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

    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        random.ints(6, 1, 10).forEach(builder::append);

        return builder.toString();
    }
}
