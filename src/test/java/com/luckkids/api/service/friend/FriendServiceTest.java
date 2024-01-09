package com.luckkids.api.service.friend;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friend.response.FriendInviteCodeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friends.Friend;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.jwt.dto.UserInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

public class FriendServiceTest extends IntegrationTestSupport {

    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendCodeRepository friendCodeRepository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        friendCodeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("친구코드를 생성한다.")
    @Test
    void inviteCodeTest() {
        List<User> users = new ArrayList<>();

        IntStream.rangeClosed(1, 2).forEach(i -> {
            User user = createUser(i);
            users.add(user);
        });


        userRepository.saveAll(users);

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(users.get(0).getId()));

        FriendInviteCodeResponse friendInviteCodeResponse = friendService.inviteCode();

        assertThat(friendInviteCodeResponse.getCode().length()).isEqualTo(8);
    }

    @DisplayName("친구코드를 생성시 존재하지 않는 사용자일시 에러가 발생한다.")
    @Test
    void inviteCodeWithoutUserTest() {
        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(1));

        assertThatThrownBy(() -> friendService.inviteCode())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 유저는 없습니다. id = 1");
    }

    private User createUser(int i) {
        return User.builder()
            .email("test" + i)
            .password("1234")
            .missionCount(i)
            .luckPhrases("행운입니다.")
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .build();
    }

    private UserInfo createUserInfo(int userId) {
        return UserInfo.builder()
            .userId(userId)
            .email("")
            .build();
    }
}
