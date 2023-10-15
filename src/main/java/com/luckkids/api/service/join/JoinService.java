package com.luckkids.api.service.join;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.repository.join.UserRepository;
import com.luckkids.api.service.join.dto.*;
import com.luckkids.domain.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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
        String authNum = generateCode();
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

    public JoinServiceResponse joinUser(JoinServiceRequest joinServiceRequest){
        User user = joinServiceRequest.createUser();
        User savedUser = userRepository.save(user);
        return JoinServiceResponse.of(savedUser);
    }

    public String generateCode() {
        StringBuilder resultNum = new StringBuilder();
        String ranNum = "";

        Random random = new Random();

        for (int i=0; i<6; i++) {
            ranNum =  Integer.toString(random.nextInt(9));
            resultNum.append(ranNum);
        }
        return resultNum.toString();
    }
}
