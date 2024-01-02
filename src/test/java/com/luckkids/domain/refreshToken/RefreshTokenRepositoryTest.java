package com.luckkids.domain.refreshToken;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class RefreshTokenRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자의 refreshToken 데이터를 모두 제거한다.")
    void deleteByUserIdTest(){
        User user = User.builder()
            .email("test@email.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .build();

        userRepository.save(user);

        RefreshToken token = RefreshToken.builder()
            .deviceId("testDevice")
            .refreshToken("testRefreshToken")
            .user(user)
            .build();

        RefreshToken savedToken =  refreshTokenRepository.save(token);
        refreshTokenRepository.deleteAllByUserId(user.getId());

        Optional<RefreshToken> findToken = refreshTokenRepository.findById(savedToken.getId());

        assertThat(findToken.isEmpty()).isTrue();
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }
}
