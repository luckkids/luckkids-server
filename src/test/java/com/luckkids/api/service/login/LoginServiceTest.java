package com.luckkids.api.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginServiceTest extends IntegrationTestSupport {

    @Autowired
    LoginService loginService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PushRepository pushRepository;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAllInBatch();
        pushRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("일반으로 등록된 사용자가 있을 경우 일반 로그인을 한다.")
    @Test
    void normalLoginIfUserExist() throws JsonProcessingException{
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .phoneNumber("01064091048")
            .build();

        User savedUser = userRepository.save(user);

        LoginServiceRequest request = LoginServiceRequest.builder()
            .email(savedUser.getEmail())
            .password(savedUser.getPassword())
            .deviceId("asdfasdfasdfsadfsf")
            .build();

        // when
        LoginResponse loginResponse = loginService.normalLogin(request);

        // then
        assertThat(loginResponse.getAccessToken()).isNotNull();
        assertThat(loginResponse.getRefreshToken()).isNotNull();
        assertThat(loginResponse.getEmail()).isEqualTo(savedUser.getEmail());
    }

    @DisplayName("SNS로 등록된 사용자가 있을 경우 일반 로그인이 되지 않는다.")
    @Test
    void normalLoginIfSnsUserExist() throws JsonProcessingException{
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.KAKAO)
            .phoneNumber("01064091048")
            .build();

        User savedUser = userRepository.save(user);

        LoginServiceRequest request = LoginServiceRequest.builder()
            .email(savedUser.getEmail())
            .password(savedUser.getPassword())
            .deviceId("asdfasdfasdfsadfsf")
            .build();

        // when
        // then
        assertThrows(LuckKidsException.class, () -> {
            loginService.normalLogin(request);
        });
    }

    @DisplayName("등록된 사용자가 없을 경우 일반 로그인을 할 시 예외를 발생시킨다.")
    @Test
    void normalLoginIfUserNotExist() throws JsonProcessingException{
        // given
        LoginServiceRequest request = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        // when
        // then
        assertThrows(LuckKidsException.class, () -> {
            loginService.normalLogin(request);
        });
    }

    @DisplayName("일반 로그인시 비밀번호가 틀렸을 경우 예외를 발생시킨다.")
    @Test
    void normalLoginIncorrectPassword() throws JsonProcessingException {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .phoneNumber("01064091048")
            .build();

        userRepository.save(user);

        LoginServiceRequest request = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("12345678")
            .deviceId("testdeviceId")
            .build();

        // when
        // then
        assertThrows(LuckKidsException.class, () -> {
            loginService.normalLogin(request);
        });
    }

    @DisplayName("다른 디바이스로 로그인 했을 시에는 refresh-token이 각 디바이스별로 저장이 된다.")
    @Test
    @Transactional
    void normalLoginOtherDeviceRefreshToken() throws JsonProcessingException, InterruptedException {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .phoneNumber("01064091048")
            .build();

        userRepository.save(user);

        LoginServiceRequest request1 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        LoginServiceRequest request2 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId2")
            .build();

        // when
        LoginResponse loginResponse1 = loginService.normalLogin(request1);
        Thread.sleep(1000); //정확한 Jwt Signiture생성 원리는 모르겠지만 짧은시간 동시에 생성되면 Token값이 같아 Sleep 1초를 줬음
        LoginResponse loginResponse2 = loginService.normalLogin(request2);

        User savedUser = userRepository.findByEmail("tkdrl8908@naver.com");
        List<RefreshToken> refreshTokens = savedUser.getRefreshTokens();

        // then
        assertThat(refreshTokens).hasSize(2)
            .extracting("refreshToken")
            .containsExactlyInAnyOrder(
                loginResponse1.getRefreshToken(),
                loginResponse2.getRefreshToken()
            );
    }

    @DisplayName("같은 디바이스로 로그인 했을 시에는 refresh-token이 기존 디바이스에 수정이 된다.")
    @Test
    @Transactional
    void normalLoginSameDeviceRefreshToken() throws JsonProcessingException, InterruptedException {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .phoneNumber("01064091048")
            .build();

        userRepository.save(user);

        LoginServiceRequest request1 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        LoginServiceRequest request2 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        // when
        LoginResponse loginResponse1 = loginService.normalLogin(request1);
        Thread.sleep(1000); //정확한 Jwt Signiture생성 원리는 모르겠지만 짧은시간 동시에 생성되면 Token값이 같아 Sleep 1초를 줬음
        LoginResponse loginResponse2 = loginService.normalLogin(request2);

        User savedUser = userRepository.findByEmail("tkdrl8908@naver.com");
        List<RefreshToken> refreshTokens = savedUser.getRefreshTokens();

        // then
        assertThat(refreshTokens).hasSize(1)
            .extracting("deviceId", "refreshToken")
            .containsExactlyInAnyOrder(
                tuple("testdeviceId", loginResponse2.getRefreshToken())
            );
    }

    @DisplayName("다른 디바이스로 로그인 했을 시에는 push-token이 각 디바이스별로 저장이 된다.")
    @Test
    @Transactional
    void normalLoginOtherDevicePushToken() throws JsonProcessingException {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .phoneNumber("01064091048")
            .build();

        userRepository.save(user);

        LoginServiceRequest request1 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        LoginServiceRequest request2 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId2")
            .build();

        // when
        loginService.normalLogin(request1);
        loginService.normalLogin(request2);

        User savedUser = userRepository.findByEmail("tkdrl8908@naver.com");
        List<Push> pushes = savedUser.getPushes();

        // then
        assertThat(pushes.size()).isEqualTo(2);
    }

    @DisplayName("같은 디바이스로 로그인 했을 시에는 push-token이 기존 디바이스에서 수정이 된다.")
    @Test
    @Transactional
    void normalLoginSameDevicePushToken() throws JsonProcessingException {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .phoneNumber("01064091048")
            .build();

        userRepository.save(user);

        LoginServiceRequest request1 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        LoginServiceRequest request2 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .build();

        // when
        loginService.normalLogin(request1);
        loginService.normalLogin(request2);

        User savedUser = userRepository.findByEmail("tkdrl8908@naver.com");
        List<Push> pushes = savedUser.getPushes();

        // then
        assertThat(pushes).hasSize(1)
            .extracting("deviceId")
            .containsExactlyInAnyOrder(
                "testdeviceId"
            );
    }
}