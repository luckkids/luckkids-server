package com.luckkids.domain.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.user.UserReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserReadService userReadService;

    @DisplayName("유저의 이메일로 유저 정보를 조회한다.")
    @Test
    void findByEmail() {
        // given
        String email = "user@daum.net";
        User user = createUser(email, "user1234!", SnsType.KAKAO);
        userRepository.save(user);

        // when
        User findUser = userReadService.findByEmail(email);

        // then
        assertThat(findUser.getEmail()).isEqualTo(email);
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }
}