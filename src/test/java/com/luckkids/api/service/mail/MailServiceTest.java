package com.luckkids.api.service.mail;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import com.luckkids.api.service.mail.request.SendPasswordServiceRequest;
import com.luckkids.api.service.mail.response.SendMailResponse;
import com.luckkids.api.service.mail.response.SendPasswordResponse;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class MailServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    void SendMailTest() {
        SendMailServiceRequest sendMailServiceRequest = SendMailServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        given(mailService.sendMail(any(SendMailServiceRequest.class)))
            .willReturn(SendMailResponse.builder()
                .authNum("123456")
                .build()
            );

        SendMailResponse sendMailResponse = mailService.sendMail(sendMailServiceRequest);

        assertThat(sendMailResponse.getAuthNum().length()).isEqualTo(6);
    }

    @Test
    void SendPasswordTest() {
        User user = createUser("tkdrl8908@test.com", "2134",SnsType.NORMAL);
        userRepository.save(user);
        SendPasswordServiceRequest sendPasswordServiceRequest = SendPasswordServiceRequest.builder()
            .email("tkdrl8908@test.com")
            .build();

        given(mailService.sendPassword(any(SendPasswordServiceRequest.class)))
            .willReturn(SendPasswordResponse.builder()
                .tempPassword("AsDWET2s24asASd")
                .build()
            );

        SendPasswordResponse sendPasswordResponse = mailService.sendPassword(sendPasswordServiceRequest);

        assertThat(sendPasswordResponse.getTempPassword().length()).isEqualTo(15);
    }

    @Test
    void generateCode() {
        given(mailService.generateCode())
            .willReturn("123456"
            );

        String authNum = mailService.generateCode();

        assertThat(authNum.length()).isEqualTo(6);
    }

    @Test
    void generateTempPassword() {
        given(mailService.generateTempPassword())
            .willReturn("AsDWET2s24asASd"
            );

        String tempPassword = mailService.generateTempPassword();

        assertThat(tempPassword.length()).isEqualTo(15);
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }
}
