package com.luckkids.api.service.join;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinSendMailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.api.service.join.response.JoinSendMailResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    public JoinCheckEmailResponse checkEmail(JoinCheckEmailServiceRequest joinCheckEmailServiceRequest){
        String email = joinCheckEmailServiceRequest.getEmail();
        User user =  userRepository.findByEmail(email);
        if(user!=null){
            user.checkSnsType();
        }
        return JoinCheckEmailResponse.of(email);
    }

    public JoinSendMailResponse sendMail(JoinSendMailServiceRequest joinSendMailServiceRequest) {
        String email = joinSendMailServiceRequest.getEmail();
        String authNum = generateCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("Luck-Maker 회원가입 인증번호"); // 메일 제목
            mimeMessageHelper.setText("인증번호: "+authNum); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            return JoinSendMailResponse.of(authNum);
        } catch (MessagingException e) {
            throw new LuckKidsException(ErrorCode.MAIL_FAIL);
        }
    }

    public JoinResponse joinUser(JoinServiceRequest joinServiceRequest){
        try {
            User user = joinServiceRequest.createUser();
            User savedUser = userRepository.save(user);

            return JoinResponse.of(savedUser);
        }catch (Exception e){
            throw new LuckKidsException(ErrorCode.USER_NORMAL);
        }
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
