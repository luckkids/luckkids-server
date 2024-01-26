package com.luckkids.domain.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.push.PushReadService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.jwt.dto.JwtToken;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.luckkids.domain.user.SettingStatus.COMPLETE;
import static com.luckkids.domain.user.SettingStatus.INCOMPLETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
public class UserTest extends IntegrationTestSupport {

    @Autowired
    private PushRepository pushRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private PushReadService pushReadService;

    @DisplayName("같은 deviceid로 저장되어있는 pushkey와 상이한 데이터가 들어올시 수정한다.")
    @Test
    void checkPushAndUpdate() {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .role(Role.USER)
            .snsType(SnsType.NORMAL)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();

        User savedUser = userRepository.save(user);

        Push push = Push.builder()
            .pushToken("testPushKey")
            .user(savedUser)
            .deviceId("testDeviceId")
            .build();

        Push savedPush = pushRepository.save(push);

        // when
        savedUser.checkPushKey("testPushKey2", "testDeviceId");

        // then
        assertThat(savedUser.getPushes().get(0))
            .extracting("pushToken", "deviceId")
             .containsExactlyInAnyOrder(
                "testPushKey2", "testDeviceId"
            );
    }

    @DisplayName("같은 deviceid로 저장되어있는 pushkey가 없을 시 저장한다.")
    @Test
    void checkPushAndSave() {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .role(Role.USER)
            .snsType(SnsType.NORMAL)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();

        User savedUser = userRepository.save(user);

        // when
        savedUser.checkPushKey("testPushKey", "testDeviceId");

        // then
        List<Push> pushes = savedUser.getPushes();

        assertThat(pushes).hasSize(1)
            .extracting("pushToken", "deviceId")
            .containsExactlyInAnyOrder(
                tuple("testPushKey", "testDeviceId")
            );
    }

    @DisplayName("같은 deviceid로 저장되어있는 refreshToken과 상이한 데이터가 들어올시 저장한다.")
    @Test
    void checkRefreshTokenAndUpdate() {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .role(Role.USER)
            .snsType(SnsType.NORMAL)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();

        User savedUser = userRepository.save(user);

        RefreshToken refreshToken = RefreshToken.builder()
            .refreshToken("testRefreshToken1")
            .deviceId("testDeviceId")
            .build();

        refreshToken.setUser(savedUser);

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        JwtToken jwtToken = JwtToken.builder()
            .accessToken("accesstoken")
            .refreshToken("testRefreshToken2")
            .grantType("Bearer")
            .expiresIn(1L)
            .build();

        // when
        savedUser.checkRefreshToken(jwtToken, "testDeviceId");

        // then
        List<RefreshToken> getToken = savedUser.getRefreshTokens();

        assertThat(getToken)
            .extracting("refreshToken", "deviceId")
            .containsExactlyInAnyOrder(
                tuple("testRefreshToken2", "testDeviceId")
            );
    }

    @DisplayName("같은 deviceid로 저장되어있는 refreshToken가 없을 시 수정한다.")
    @Test
    void checkRefreshTokenAndSave() {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .role(Role.USER)
            .snsType(SnsType.NORMAL)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();

        User savedUser = userRepository.save(user);

        JwtToken jwtToken = JwtToken.builder()
            .accessToken("accesstoken")
            .refreshToken("testRefreshToken")
            .grantType("Bearer")
            .expiresIn(1L)
            .build();

        // when
        savedUser.checkRefreshToken(jwtToken, "testDeviceId");

        // then
        List<RefreshToken> getToken = savedUser.getRefreshTokens();

        assertThat(getToken).hasSize(1)
            .extracting("refreshToken", "deviceId")
            .containsExactlyInAnyOrder(
                tuple("testRefreshToken", "testDeviceId")
            );
    }

    @DisplayName("사용자 비밀번호를 변경한다.")
    @Test
    @Transactional
    void updatePasswordTest() {
        User user = createUser("test@email.com", "1234", SnsType.NORMAL);
        String beforePassword = user.getPassword();
        User savedUser = userRepository.save(user);
        savedUser.updatePassword("123456");
        String afterPassword = savedUser.getPassword();

        assertThat(beforePassword).isNotEqualTo(afterPassword);
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }

    @DisplayName("사용자의 행운문구를 수정한다.")
    @Test
    void updateLuckPhrasesTest() {
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .role(Role.USER)
            .snsType(SnsType.NORMAL)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();

        User savedUser = userRepository.save(user);

        // when
        User findUser = userReadService.findByOne(savedUser.getId());
        findUser.updateSettingStatus(COMPLETE);

        // then
        assertThat(findUser.getSettingStatus()).isEqualTo(COMPLETE);
        savedUser.updateLuckPhrases("행운입니다!!");
        savedUser.updateSettingStatus(INCOMPLETE);

        // then
        assertThat(savedUser)
            .extracting("email", "luckPhrases", "role", "snsType", "settingStatus")
            .contains(
                "tkdrl8908@naver.com", "행운입니다!!", Role.USER, SnsType.NORMAL, SettingStatus.INCOMPLETE
            );
    }
}
