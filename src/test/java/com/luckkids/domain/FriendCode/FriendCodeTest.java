package com.luckkids.domain.FriendCode;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.UseStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class FriendCodeTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendCodeRepository friendCodeRepository;

    @DisplayName("이미 사용한 친구코드일 시 예외가 발생한다.")
    @Test
    void checkPushAndUpdate() {
        User savedUser = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL));

        FriendCode friendCode = friendCodeRepository.save(createFriendCode(savedUser));

        assertThatThrownBy(friendCode::checkUsed)
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("이미 사용된 친구코드입니다.");
    }

    private FriendCode createFriendCode(User user){
        return FriendCode.builder()
            .code("ABSDEDGD")
            .useStatus(UseStatus.USED)
            .user(user)
            .build();
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }
}
