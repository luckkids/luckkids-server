package com.luckkids.api.service.friendCode;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.friendCode.request.FriendCodeNickNameServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCodeNickNameResponse;
import com.luckkids.api.service.friendCode.response.FriendRefuseResponse;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.FriendStatus;
import com.luckkids.domain.friendCode.UseStatus;
import com.luckkids.domain.user.*;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class FriendCodeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendCodeRepository friendCodeRepository;
    @Autowired
    private FriendCodeReadService friendCodeReadService;
    @Autowired
    private FriendRepository friendRepository;

    @AfterEach
    void tearDown(){
        friendCodeRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("FriendCode Entity를 조회한다.")
    @Test
    void findByCode(){
        User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
        User savedUser = userRepository.save(user);

        FriendCode code = createFriendCode(savedUser);
        friendCodeRepository.save(code);

        FriendCode findCode = friendCodeReadService.findByCode("ABCDEFGH");
        assertThat(findCode).extracting("code", "useStatus")
                .contains("ABCDEFGH", UseStatus.UNUSED);
    }

    @DisplayName("FriendCode로 nickname과 상태값을 조회한다.")
    @Test
    void findNickNameByCode(){
        // given
        User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
        User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 0);
        User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 0);

        userRepository.saveAll(List.of(user1, user2, user3));

        given(securityService.getCurrentLoginUserInfo())
                .willReturn(createLoginUserInfo(user1.getId()));

        Friend friend1 = createFriend(user1, user2);
        Friend friend2 = createFriend(user2, user1);
        friendRepository.saveAll(List.of(friend1, friend2));

        FriendCode code = createFriendCode(user2);
        friendCodeRepository.save(code);

        FriendCodeNickNameServiceRequest friendCodeNickNameServiceRequest = FriendCodeNickNameServiceRequest.builder()
                .code("ABCDEFGH")
                .build();

        FriendCodeNickNameResponse friendCodeNickNameResponse = friendCodeReadService.findNickNameByCode(friendCodeNickNameServiceRequest);

        assertThat(friendCodeNickNameResponse).extracting("nickName", "friendStatus")
                .contains("테스트2", FriendStatus.ALREADY);
    }


    private User createUser(String email, String password, String nickname, String luckPhrase, int missionCount) {
        return User.builder()
                .email(email)
                .password(password)
                .snsType(SnsType.NORMAL)
                .nickname(nickname)
                .luckPhrase(luckPhrase)
                .role(Role.USER)
                .settingStatus(SettingStatus.COMPLETE)
                .missionCount(missionCount)
                .build();
    }

    private Friend createFriend(User requester, User receiver) {
        return Friend.builder()
                .requester(requester)
                .receiver(receiver)
                .build();
    }

    private FriendCode createFriendCode(User user){
        return FriendCode.builder()
            .user(user)
            .code("ABCDEFGH")
            .useStatus(UseStatus.UNUSED)
            .build();
    }

    private LoginUserInfo createLoginUserInfo(int userId) {
        return LoginUserInfo.builder()
                .userId(userId)
                .build();
    }

}
