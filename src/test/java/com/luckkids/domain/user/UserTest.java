package com.luckkids.domain.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.jwt.dto.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
public class UserTest extends IntegrationTestSupport {

    @Autowired
    PushRepository pushRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @DisplayName("같은 deviceid로 저장되어있는 pushkey와 상이한 데이터가 들어올시 수정한다.")
    @Test
    void checkPushAndUpdate(){
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .phoneNumber("01064091048")
            .role(Role.USER)
            .snsType(SnsType.NORMAL)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();

        User savedUser = userRepository.save(user);

        Push push = Push.builder()
            .pushToken("testPushKey")
            .deviceId("testDeviceId")
            .build();

        push.setUser(savedUser);

        Push savedPush = pushRepository.save(push);

        // when
        savedUser.checkPushKey("testPushKey2", "testDeviceId");

        // then
        Push getPush = pushRepository.findById(savedPush.getId()).get();

        assertThat(getPush)
            .extracting("pushToken", "deviceId")
            .containsExactlyInAnyOrder(
               "testPushKey2", "testDeviceId"
            );
    }

    @DisplayName("같은 deviceid로 저장되어있는 pushkey가 없을 시 저장한다.")
    @Test
    void checkPushAndSave(){
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .phoneNumber("01064091048")
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
    void checkRefreshTokenAndUpdate(){
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .phoneNumber("01064091048")
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
        RefreshToken getToken = refreshTokenRepository.findById(savedToken.getId()).get();

        assertThat(getToken)
            .extracting("refreshToken", "deviceId")
            .containsExactlyInAnyOrder(
                "testRefreshToken2", "testDeviceId"
            );
    }

    @DisplayName("같은 deviceid로 저장되어있는 refreshToken가 없을 시 저장한다.")
    @Test
    void checkRefreshTokenAndSave(){
        // given
        User user = User.builder()
            .email("tkdrl8908@naver.com")
            .password("1234")
            .phoneNumber("01064091048")
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
}