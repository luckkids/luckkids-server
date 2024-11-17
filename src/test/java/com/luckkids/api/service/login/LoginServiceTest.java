package com.luckkids.api.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.feign.google.response.GoogleUserInfoResponse;
import com.luckkids.api.feign.kakao.response.KakaoUserInfoResponse;
import com.luckkids.api.service.alertSetting.AlertSettingReadService;
import com.luckkids.api.service.login.request.LoginGenerateTokenServiceRequest;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.request.OAuthLoginServiceRequest;
import com.luckkids.api.service.login.response.LoginGenerateTokenResponse;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.api.service.login.response.OAuthLoginResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.JwtTokenGenerator;
import com.luckkids.jwt.dto.JwtToken;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.luckkids.domain.misson.AlertStatus.CHECKED;
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

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private AlertSettingReadService alertSettingReadService;

    @Autowired
    private AlertSettingRepository alertSettingRepository;

    @Autowired
    private SecurityService securityService;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAllInBatch();
        alertSettingRepository.deleteAllInBatch();
        pushRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("일반으로 등록된 사용자가 있을 경우 일반 로그인을 한다.")
    @Test
    void normalLoginIfUserExist() throws JsonProcessingException {
        // given
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);

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
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);

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
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);

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
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);

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
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE);

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

    @Test
    @DisplayName("리플래쉬토큰으로 엑세스토큰을 재발급한다.")
    void generateJwtToken() throws JsonProcessingException {
        User savedUser = userRepository.save(createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE));

        LoginUserInfo userInfo = LoginUserInfo.builder()
            .userId(savedUser.getId())
            .build();

        JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);

        refreshTokenRepository.save(createRefreshToken(savedUser, jwtToken.getRefreshToken()));

        LoginGenerateTokenServiceRequest loginGenerateTokenServiceRequest = LoginGenerateTokenServiceRequest.builder()
            .email(savedUser.getEmail())
            .deviceId("testDeviceId")
            .refreshToken(jwtToken.getRefreshToken())
            .build();

        LoginGenerateTokenResponse loginGenerateTokenResponse = loginService.refreshJwtToken(loginGenerateTokenServiceRequest);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserIdAndDeviceIdAndRefreshToken(savedUser.getId(), "testDeviceId", loginGenerateTokenResponse.getRefreshToken());

        assertThat(refreshToken.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("리플래쉬토큰으로 엑세스토큰을 재발급할시 해당 deviceId가 존재하지않을시 예외가 발생한다.")
    void generateAccessTokenNotExistDeviceId() throws JsonProcessingException {
        User savedUser = userRepository.save(createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE));

        LoginUserInfo userInfo = LoginUserInfo.builder()
            .userId(savedUser.getId())
            .build();

        JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);

        refreshTokenRepository.save(createRefreshToken(savedUser, jwtToken.getRefreshToken()));

        LoginGenerateTokenServiceRequest loginGenerateTokenServiceRequest = LoginGenerateTokenServiceRequest.builder()
            .email(savedUser.getEmail())
            .deviceId("testDeviceId2")
            .refreshToken(jwtToken.getRefreshToken())
            .build();

        assertThatThrownBy(() -> loginService.refreshJwtToken(loginGenerateTokenServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("리플래시 토큰이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("리플래쉬토큰으로 엑세스토큰을 재발급할시 해당 이메일이 존재하지않을시 예외가 발생한다.")
    void generateAccessTokenNotExistUser() throws JsonProcessingException {
        User savedUser = userRepository.save(createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.INCOMPLETE));

        LoginUserInfo userInfo = LoginUserInfo.builder()
            .userId(savedUser.getId())
            .build();

        JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);

        refreshTokenRepository.save(createRefreshToken(savedUser, jwtToken.getRefreshToken()));

        LoginGenerateTokenServiceRequest loginGenerateTokenServiceRequest = LoginGenerateTokenServiceRequest.builder()
            .email("test@test.com")
            .deviceId("testDeviceId")
            .refreshToken(jwtToken.getRefreshToken())
            .build();

        assertThatThrownBy(() -> loginService.refreshJwtToken(loginGenerateTokenServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("해당 이메일을 사용중인 사용자가 존재하지 않습니다.");
    }

    @DisplayName("카카오 로그인을 한다.")
    @Test
    @Transactional
    void oauthKakaoLoginTest() throws JsonProcessingException {
        // given
        User user = createUser("test@test.com", "1234", SnsType.KAKAO, SettingStatus.INCOMPLETE);

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

        OAuthLoginResponse oAuthLoginResponse = loginService.oauthLogin(oAuthLoginServiceRequest);

        assertThat(oAuthLoginResponse.getAccessToken()).isNotNull();
        assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull();
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com");
    }

    @DisplayName("구글 로그인을 한다.")
    @Test
    @Transactional
    void oauthGoogleLoginTest() throws JsonProcessingException {
        // given
        User user = createUser("test@test.com", "1234", SnsType.GOOGLE, SettingStatus.INCOMPLETE);

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

        OAuthLoginResponse oAuthLoginResponse = loginService.oauthLogin(oAuthLoginServiceRequest);

        assertThat(oAuthLoginResponse.getAccessToken()).isNotNull();
        assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull();
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com");
    }

    @DisplayName("카카오 로그인을 할 시 이미 등록되어있는 이메일이라면 예외가 발생한다.")
    @Test
    @Transactional
    void oauthKakaoLoginExistTest() {
        // given
        User user = createUser("test@test.com", "1234", SnsType.GOOGLE, SettingStatus.INCOMPLETE);

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

        OAuthLoginResponse oAuthLoginResponse = loginService.oauthLogin(oAuthLoginServiceRequest);

        User user = userReadService.findByEmail("test@test.com");

        assertThat(oAuthLoginResponse.getAccessToken()).isNotNull();
        assertThat(oAuthLoginResponse.getRefreshToken()).isNotNull();
        assertThat(oAuthLoginResponse.getEmail()).isEqualTo("test@test.com");
    }

    @DisplayName("새로운 디바이스로 로그인을 하면 ALERTSETTING을 생성한다.")
    @Test
    void loginNewDeviceCreateAlertSetting() throws JsonProcessingException {
        // given
        User user = createUser("tkdrl8908@naver.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE);

        User savedUser = userRepository.save(user);

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email(savedUser.getEmail())
                .password("1234")
                .deviceId("asdfasdfasdfsadfsf")
                .pushKey("testToken")
                .build();

        loginService.normalLogin(request);

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createUserInfo(savedUser.getId()));

        // when
        // then
        AlertSetting alertSetting =  alertSettingReadService.findOneByDeviceIdAndUserId("asdfasdfasdfsadfsf");
        assertThat(alertSetting).extracting("entire", "mission", "notice", "friend", "luckMessage")
                .contains(CHECKED, CHECKED, CHECKED, CHECKED, CHECKED);
    }

    User createUser(String email, String password, SnsType snsType, SettingStatus settingStatus) {
        return User.builder()
            .email(email)
            .password(bCryptPasswordEncoder.encode(password))
            .snsType(snsType)
            .settingStatus(settingStatus)
            .build();
    }

    RefreshToken createRefreshToken(User user, String refreshToken) {
        return RefreshToken.builder()
            .user(user)
            .deviceId("testDeviceId")
            .refreshToken(refreshToken)
            .build();
    }

    private LoginUserInfo createUserInfo(int userId) {
        return LoginUserInfo.builder()
                .userId(userId)
                .build();
    }
}
