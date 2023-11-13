package com.luckkids.api.service.join;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class JoinServiceTest extends IntegrationTestSupport {

    @Autowired
    private JoinService joinService;

    @Autowired
    private JoinReadService joinReadService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("이메일이 이미 등록되었는지 체크한다.")
    @Test
    void checkEmailTest() {
        JoinCheckEmailServiceRequest joinCheckEmailServiceRequest = JoinCheckEmailServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        JoinCheckEmailResponse response = joinReadService.checkEmail(joinCheckEmailServiceRequest);

        assertThat(response.getEmail()).isEqualTo(joinCheckEmailServiceRequest.getEmail());
    }

    @DisplayName("사용자가 이미 존재 할 경우 예외를 던진다.")
    @Test
    void checkEmailIfExistUser() {
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .build();

        userRepository.save(user);

        JoinCheckEmailServiceRequest joinCheckEmailServiceRequest = JoinCheckEmailServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .build();

        assertThrows(LuckKidsException.class, () -> joinReadService.checkEmail(joinCheckEmailServiceRequest));
    }

    @DisplayName("회원가입 테스트")
    @Test
    void JoinTest() {
        JoinServiceRequest joinServiceRequest = JoinServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .build();

        JoinResponse response = joinService.joinUser(joinServiceRequest);

        assertThat(response)
            .extracting(
                "email",
                "snsType"
            )
            .contains(
                joinServiceRequest.getEmail(),
                SnsType.NORMAL
            );
    }

    @DisplayName("회원가입시 비밀번호가 단방향 암호화가 되었는지 체크한다.")
    @Test
    void JoinTestWithEncryptPassword() {
        JoinServiceRequest joinServiceRequest = JoinServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .build();

        JoinResponse response = joinService.joinUser(joinServiceRequest);

        User user = userRepository.findByEmail(response.getEmail());
        String password = user.getPassword();
        String encryptPassword = user.encryptPassword("1234");

        assertThat(password).isEqualTo(encryptPassword);
    }
}
