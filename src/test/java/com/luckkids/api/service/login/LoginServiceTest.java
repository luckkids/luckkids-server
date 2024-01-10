package com.luckkids.api.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.feign.google.response.GoogleUserInfoResponse;
import com.luckkids.api.feign.kakao.response.KakaoUserInfoResponse;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.request.OAuthLoginServiceRequest;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.api.service.login.response.OAuthLoginResponse;
import com.luckkids.api.service.user.UserReadService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class LoginServiceTest extends IntegrationTestSupport {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PushRepository pushRepository;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAllInBatch();
        pushRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("일반으로 등록된 사용자가 있을 경우 일반 로그인을 한다.")
    @Test
    void normalLoginIfUserExist() throws JsonProcessingException {
        // given
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL);

        User savedUser = userRepository.save(user);

        LoginServiceRequest request = LoginServiceRequest.builder()
            .email(savedUser.getEmail())
            .password("1234")
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
    void normalLoginIfSnsUserExist() {
        // given
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.KAKAO);

        User savedUser = userRepository.save(user);

        LoginServiceRequest request = LoginServiceRequest.builder()
            .email(savedUser.getEmail())
            .password("1234")
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
    void normalLoginIfUserNotExist() {
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
    void normalLoginIncorrectPassword() {
        // given
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL);

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

    @DisplayName("다른 디바이스로 로그인 했을 시에는 push-token과 refreshToken이 각 디바이스별로 저장이 된다.")
    @Test
    @Transactional
    void normalLoginOtherDevicePushToken() throws JsonProcessingException {
        // given
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL);

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
        LoginResponse loginResponse2 = loginService.normalLogin(request2);

        User savedUser = userReadService.findByEmail("tkdrl8908@naver.com");
        List<Push> pushes = savedUser.getPushes();
        List<RefreshToken> refreshTokens = savedUser.getRefreshTokens();

        // then
        assertThat(refreshTokens).hasSize(2)
            .extracting("refreshToken")
            .containsExactlyInAnyOrder(
                loginResponse1.getRefreshToken(),
                loginResponse2.getRefreshToken()
            );
        assertThat(pushes.size()).isEqualTo(2);
    }

    @DisplayName("같은 디바이스로 로그인 했을 시에는 push-token과 refreshToken이 기존 디바이스에서 수정이 된다.")
    @Test
    @Transactional
    void normalLoginSameDevicePushToken() throws JsonProcessingException {
        // given
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL);

        userRepository.save(user);

        LoginServiceRequest request1 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .pushKey("testPushKey")
            .build();

        LoginServiceRequest request2 = LoginServiceRequest.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .deviceId("testdeviceId")
            .pushKey("testPushKey2")
            .build();

        // when
        loginService.normalLogin(request1);
        loginService.normalLogin(request2);

        User savedUser = userReadService.findByEmail("tkdrl8908@naver.com");
        List<Push> pushes = savedUser.getPushes();
        List<RefreshToken> refreshTokens = savedUser.getRefreshTokens();

        // then
        assertThat(refreshTokens).hasSize(1)
            .extracting("deviceId")
            .containsExactlyInAnyOrder(
                "testdeviceId"
            );
        assertThat(pushes).hasSize(1)
            .extracting("deviceId")
            .containsExactlyInAnyOrder(
                "testdeviceId"
            );
    }

    @DisplayName("카카오 로그인을 한다.")
    @Test
    @Transactional
    void oauthKakaoLoginTest() throws JsonProcessingException {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO);

        userRepository.save(user);

        given(kakaoApiFeignCall.getUserInfo(any(String.class)))
            .willReturn(
                KakaoUserInfoResponse.builder()
                    .kakaoAccount(KakaoUserInfoResponse.KakaoAccount.builder()
                        .email("test@test.com")
                        .build())
                    .build()
            );

        OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
            .token("sadhAewofneonfoweifkpowekfkajfbdsnflksndfdsmfkl")
            .deviceId("testDeviceId")
            .pushKey("testPushKey")
            .snsType(SnsType.KAKAO)
            .build();

        OAuthLoginResponse oAuthLoginResponse =  loginService.oauthLogin(oAuthLoginServiceRequest);

        assertThat(oAuthLoginResponse.getAccessToken()).isNotNull();
        assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull();
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com");
    }

    @DisplayName("구글 로그인을 한다.")
    @Test
    @Transactional
    void oauthGoogleLoginTest() throws JsonProcessingException {
        // given
        User user = createUser("test@test.com", "1234", SnsType.GOOGLE);

        userRepository.save(user);

        given(googleApiFeignCall.getUserInfo(any(String.class)))
            .willReturn(
                GoogleUserInfoResponse.builder()
                    .email("test@test.com")
                    .build()
            );

        OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
            .token("sadhAewofneonfoweifkpowekfkajfbdsnflksndfdsmfkl")
            .deviceId("testDeviceId")
            .pushKey("testPushKey")
            .snsType(SnsType.GOOGLE)
            .build();

        OAuthLoginResponse oAuthLoginResponse =  loginService.oauthLogin(oAuthLoginServiceRequest);

        assertThat(oAuthLoginResponse.getAccessToken()).isNotNull();
        assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull();
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com");
    }

    @DisplayName("애플 로그인을 한다.")
    @Test
    @Transactional
    void oauthAppleLoginTest() throws JsonProcessingException {
        // given
        User user = createUser("koyrkr@gmail.com", "1234", SnsType.APPLE);

        userRepository.save(user);

        OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
            .token("eyJraWQiOiJXNldjT0tCIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLmFwcC5sdWNrLWtpZHMiLCJleHAiOjE3MDQ4NzAwNjUsImlhdCI6MTcwNDc4MzY2NSwic3ViIjoiMDAwMjQ0LmExMjZmMmJmMGEwODQxZmZhNzlmYmRiN2JjNTE0ZjA2LjE1NDkiLCJub25jZSI6IjU4NGFiYmQzN2FlZGI1MmRjOTdjMGM1YzY4YmYwMDc3Njc3ZGYwYzVmOTcwODdjYWUwZGFiNWUwM2ZlMzY3YjAiLCJjX2hhc2giOiJaR1VsbWlnRXpORXE4RVNDZXJTSGVBIiwiZW1haWwiOiJrb3lya3JAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiYXV0aF90aW1lIjoxNzA0NzgzNjY1LCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.WNUth8x-erECYqVJCu_KcldR_aPOlcYhY1-_BWNbPMGjtFxTYCF_blz49j71gu_qzfz-HMLfH9isxemHyDJIiiHS7jLkQucKSj_i0EDHv_37grmJODygiUg2X-vjNRhBKt0LBC7YeAoD9NR-_TOxg7TknSDW4keeNGx-PB43jVtZgvrudca7bSdZRfTh2VsJTqLCTDAeOfbNnP15Gcdq0TgTaWHQCUWfqBZ1i0hxxJ2jVrlGqRbvRwocRYKtc5D_OULz5eBGJ41A0r0kPDJWhoU0UMM-Vzwkqt4CdM777cnrWeu6qwk6Nwk5Ae1g2yI2GpSH_y_400eCTWRCfQvUJg")
            .deviceId("testDeviceId")
            .pushKey("testPushKey")
            .snsType(SnsType.APPLE)
            .build();

        OAuthLoginResponse oAuthLoginResponse =  loginService.oauthLogin(oAuthLoginServiceRequest);

        assertThat(oAuthLoginResponse.getAccessToken()).isNotNull();
        assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull();
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo("koyrkr@gmail.com");
    }

    @DisplayName("카카오 로그인을 할 시 이미 등록되어있는 이메일이라면 예외가 발생한다.")
    @Test
    @Transactional
    void oauthKakaoLoginExistTest(){
        // given
        User user = createUser("test@test.com", "1234", SnsType.GOOGLE);

        userRepository.save(user);

        given(kakaoApiFeignCall.getUserInfo(any(String.class)))
            .willReturn(
                KakaoUserInfoResponse.builder()
                    .kakaoAccount(KakaoUserInfoResponse.KakaoAccount.builder()
                        .email("test@test.com")
                        .build())
                    .build()
            );

        OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
            .token("sadhAewofneonfoweifkpowekfkajfbdsnflksndfdsmfkl")
            .deviceId("testDeviceId")
            .pushKey("testPushKey")
            .snsType(SnsType.KAKAO)
            .build();

        assertThatThrownBy(() -> loginService.oauthLogin(oAuthLoginServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("GOOGLE");
    }

    @DisplayName("카카오 로그인을 할 시 저장된 사용자가 없으면 회원가입한다.")
    @Test
    @Transactional
    void oauthKakaoJoinTest() throws JsonProcessingException {
        // given
        given(kakaoApiFeignCall.getUserInfo(any(String.class)))
            .willReturn(
                KakaoUserInfoResponse.builder()
                    .kakaoAccount(KakaoUserInfoResponse.KakaoAccount.builder()
                        .email("test@test.com")
                        .build())
                    .build()
            );

        OAuthLoginServiceRequest oAuthLoginServiceRequest = OAuthLoginServiceRequest.builder()
            .token("sadhAewofneonfoweifkpowekfkajfbdsnflksndfdsmfkl")
            .deviceId("testDeviceId")
            .pushKey("testPushKey")
            .snsType(SnsType.KAKAO)
            .build();

        OAuthLoginResponse oAuthLoginResponse =  loginService.oauthLogin(oAuthLoginServiceRequest);

        User user = userReadService.findByEmail("test@test.com");

        assertThat(oAuthLoginResponse.getAccessToken()).isNotNull();
        assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull();
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com");
    }

    User createUser(String email, String password, SnsType snsType){
        return User.builder()
            .email(email)
            .password(bCryptPasswordEncoder.encode(password))
            .snsType(snsType)
            .build();
    }
}
