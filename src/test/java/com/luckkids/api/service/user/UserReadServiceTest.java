package com.luckkids.api.service.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.user.request.UserFindEmailServiceRequest;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        .contains("user@daum.net", user.getPassword() , SnsType.KAKAO);

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

    @DisplayName("이메일을 받아서 해당 이메일을 사용하는 사용자를 가져온다")
    @Test
    void findByEmail() {
        // given
        User user = createUser("test@email.com", "1234", SnsType.NORMAL);
        userRepository.save(user);
        // when
        User findUser = userReadService.findByEmail(user.getEmail());
        // then
        assertThat(findUser).extracting("email", "password", "snsType")
            .contains("test@email.com", user.getPassword(), SnsType.NORMAL);
    }

    @DisplayName("이메일을 받아서 해당 이메일을 사용하는 사용자를 조회시 존재하지 않는 사용자일 경우 예외를 발생시킨다.")
    @Test
    void findByEmailThrowException() {
        // given

        // when then
        assertThatThrownBy(() -> userReadService.findByEmail("test@email.com"))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("해당 이메일을 사용중인 사용자가 존재하지 않습니다.");
    }

    @DisplayName("입력받은 이메일의 회원가입 형태를 확인한다.")
    @Test
    void findSnsTypeTest(){
        User user = createUser("test@email.com", "1234", SnsType.KAKAO);
        userRepository.save(user);

        UserFindEmailServiceRequest userFindEmailServiceRequest = UserFindEmailServiceRequest.builder()
            .email("test@email.com")
            .build();

        UserFindSnsTypeResponse userFindSnsTypeResponse = userReadService.findEmail(userFindEmailServiceRequest);

        assertThat(userFindSnsTypeResponse.getSnsType()).isEqualTo(SnsType.KAKAO);
    }

    @DisplayName("입력받은 이메일의 회원가입 형태를 확인시 사용자가 존재하지않으면 예외를 발생시킨다.")
    @Test
    void findSnsTypeTestThrowException(){
        UserFindEmailServiceRequest userFindEmailServiceRequest = UserFindEmailServiceRequest.builder()
            .email("test@email.com")
            .build();

        assertThatThrownBy(() -> userReadService.findEmail(userFindEmailServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("해당 이메일을 사용중인 사용자가 존재하지 않습니다.");
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(bCryptPasswordEncoder.encode(password))
            .snsType(snsType)
            .build();
    }
}