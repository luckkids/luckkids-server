package com.luckkids.api.service.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserReadServiceTest extends IntegrationTestSupport {

    @Autowired
    UserReadService userReadService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저의 id값을 받아서 유저가 있는지 검색한다.")
    @Test
    void findByOne() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
        User savedUser = userRepository.save(user);
        int userId = savedUser.getId();

        // when
        User result = userReadService.findByOne(userId);

        // then
        assertThat(result)
            .extracting("email", "password", "snsType")
            .contains("user@daum.net", result.encryptPassword("user1234!") , SnsType.KAKAO);

    }

    @DisplayName("유저의 id값을 받아서 유저가 있는지 검색했을 때 없는 유저의 예외상황이 발생한다.")
    @Test
    void findByOneWithException() {
        // given
        int userId = 0;

        // when // then
        assertThatThrownBy(() -> userReadService.findByOne(userId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 유저는 없습니다. id = " + userId);

    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }
}