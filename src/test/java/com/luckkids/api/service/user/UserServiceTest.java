package com.luckkids.api.service.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserReadService userReadService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자 비밀번호를 변경한다.")
    @Test
    void changePasswordTest(){
        User user = createUser("test@email.com", "1234", SnsType.NORMAL);
        userRepository.save(user);

        UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest = UserUpdatePasswordServiceRequest.builder()
            .email("test@email.com")
            .password("123456")
            .build();

        UserUpdatePasswordResponse userChangePasswordResponse = userService.updatePassword(userUpdatePasswordServiceRequest);

        User findUser = userReadService.findByOne(user.getId());

        assertThat(userChangePasswordResponse.getEmail()).isEqualTo("test@email.com");
        assertThat(user.getPassword()).isNotEqualTo(findUser.getPassword());
    }

    @DisplayName("사용자 비밀번호를 변경시 사용자가 존재하지않으면 예외를 발생시킨다.")
    @Test
    void changePasswordTestThrowException(){
        UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest = UserUpdatePasswordServiceRequest.builder()
            .email("test@email.com")
            .password("123456")
            .build();

        assertThatThrownBy(() -> userService.updatePassword(userUpdatePasswordServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("해당 이메일을 사용중인 사용자가 존재하지 않습니다.");
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }
}
