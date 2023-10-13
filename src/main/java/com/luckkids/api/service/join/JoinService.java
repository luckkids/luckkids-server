package com.luckkids.api.service.join;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.repository.join.UserRepository;
import com.luckkids.api.service.join.dto.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.dto.JoinSendMailServiceRequest;
import com.luckkids.api.service.join.dto.JoinSendMailServiceResponse;
import com.luckkids.domain.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    public void checkEmail(JoinCheckEmailServiceRequest joinCheckEmailServiceRequest){
        String email = joinCheckEmailServiceRequest.getEmail();
        User user =  userRepository.findByEmail(email);
        if(user!=null){
            user.checkSnsType();
        }
    }

    public JoinSendMailServiceResponse sendMail(JoinSendMailServiceRequest joinSendMailServiceRequest) {
        String email = joinSendMailServiceRequest.getEmail();
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("테스트"); // 메일 제목
            mimeMessageHelper.setText("테스트"); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            return JoinSendMailServiceResponse.builder()
                    .code(authNum)
                    .build();
        } catch (MessagingException e) {
            throw new LuckKidsException(ErrorCode.MAIL_FAIL);
        }
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
}
