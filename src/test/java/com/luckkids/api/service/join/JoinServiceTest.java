package com.luckkids.api.service.join;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinSendMailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.api.service.join.response.JoinSendMailResponse;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class JoinServiceTest extends IntegrationTestSupport {

    @Autowired
    JoinService joinService;

    @Autowired
    JoinReadService joinReadService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    void SendMailTest(){
        JoinSendMailServiceRequest joinSendMailServiceRequest = JoinSendMailServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        JoinSendMailResponse joinSendMailResponse = joinReadService.sendMail(joinSendMailServiceRequest);

        assertThat(joinSendMailResponse.getAuthNum().length()).isEqualTo(6);
    }

    @Test
    void checkEmailTest(){
        JoinCheckEmailServiceRequest joinCheckEmailServiceRequest = JoinCheckEmailServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        JoinCheckEmailResponse response = joinReadService.checkEmail(joinCheckEmailServiceRequest);

        assertThat(response.getEmail()).isEqualTo(joinCheckEmailServiceRequest.getEmail());
    }

    @Test
    void JoinTest(){
        JoinServiceRequest joinServiceRequest = JoinServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .phoneNumber("01012341234")
            .build();

        JoinResponse response = joinService.joinUser(joinServiceRequest);

        assertThat(response)
            .extracting(
                "email",
                "password",
                "phoneNumber",
                "snsType",
                "role")
            .contains(
                joinServiceRequest.getEmail(),
                joinServiceRequest.getPassword(),
                joinServiceRequest.getPhoneNumber(),
                SnsType.NORMAL,
                Role.USER
            );
    }
}