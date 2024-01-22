package com.luckkids.api.service.friend;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.page.response.PageableCustom;
import com.luckkids.api.service.friend.response.FriendListResponse;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.luckkids.domain.user.*;
import com.luckkids.domain.user.projection.MyProfileDto;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;

class FriendReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private FriendReadService friendReadService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자의 친구 리스트를 조회한다.")
    @Test
    void getFriendList() {
        // given
        User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구");
        User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구");
        User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구");
        userRepository.saveAll(List.of(user1, user2, user3));

        UserCharacter userCharacter1 = createUserCharacter(user1, 1, "캐릭터1.json");
        UserCharacter userCharacter2 = createUserCharacter(user2, 1, "캐릭터2.json");
        UserCharacter userCharacter3 = createUserCharacter(user3, 1, "캐릭터2.json");
        userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2, userCharacter3));

        Friend friend1 = createFriend(user1, user2);
        Friend friend2 = createFriend(user1, user3);
        Friend friend3 = createFriend(user2, user3);
        friendRepository.saveAll(List.of(friend1, friend2, friend3));

        PageInfoServiceRequest request = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createLoginUserInfo(user1.getId()));

        // when
        FriendListResponse response = friendReadService.getFriendList(request);

        // then
        MyProfileDto myProfile = response.getMyProfile();
        assertThat(myProfile)
            .extracting("myId", "nickname", "luckPhrases", "fileUrl", "characterCount")
            .containsExactlyInAnyOrder(
                user1.getId(), "테스트1", "테스트1의 행운문구", "캐릭터1.json", 0
            );

        PageCustom<FriendProfileDto> friendPagingList = response.getFriendList();

        List<FriendProfileDto> friendList = friendPagingList.getContent();
        assertThat(friendList)
            .extracting("friendId", "nickname", "luckPhrases", "fileUrl", "characterCount")
            .containsExactlyInAnyOrder(
                tuple(user2.getId(), "테스트2", "테스트2의 행운문구", "캐릭터2.json", 0),
                tuple(user3.getId(), "테스트3", "테스트3의 행운문구", "캐릭터2.json", 0)
            );

        PageableCustom pageInfo = friendPagingList.getPageInfo();
        assertThat(pageInfo)
            .extracting("currentPage", "totalPage", "totalElement")
            .containsExactlyInAnyOrder(
                1, 1, 2L
            );
    }

    @DisplayName("사용자의 친구가 없을 시 빈리스트를 반환한다.")
    @Test
    void readListWithoutFriend() {
        // given
        User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구");
        User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구");
        User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구");
        userRepository.saveAll(List.of(user1, user2, user3));

        UserCharacter userCharacter1 = createUserCharacter(user1, 1, "캐릭터1.json");
        UserCharacter userCharacter2 = createUserCharacter(user2, 1, "캐릭터2.json");
        UserCharacter userCharacter3 = createUserCharacter(user3, 1, "캐릭터2.json");
        userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2, userCharacter3));

        Friend friend1 = createFriend(user1, user2);
        Friend friend2 = createFriend(user1, user3);
        friendRepository.saveAll(List.of(friend1, friend2));

        PageInfoServiceRequest request = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createLoginUserInfo(user2.getId()));

        // when
        FriendListResponse response = friendReadService.getFriendList(request);

        // then
        MyProfileDto myProfile = response.getMyProfile();
        assertThat(myProfile)
            .extracting("myId", "nickname", "luckPhrases", "fileUrl", "characterCount")
            .contains(
                user2.getId(), "테스트2", "테스트2의 행운문구", "캐릭터2.json", 0
            );

        PageCustom<FriendProfileDto> friendPagingList = response.getFriendList();

        List<FriendProfileDto> friendList = friendPagingList.getContent();
        assertThat(friendList).hasSize(0).isEmpty();

        PageableCustom pageInfo = friendPagingList.getPageInfo();
        assertThat(pageInfo)
            .extracting("currentPage", "totalPage", "totalElement")
            .containsExactlyInAnyOrder(
                1, 0, 0L
            );
    }

    private User createUser(String email, String password, String nickname, String luckPhrases) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(SnsType.NORMAL)
            .nickname(nickname)
            .luckPhrases(luckPhrases)
            .role(Role.USER)
            .settingStatus(SettingStatus.COMPLETE)
            .missionCount(0)
            .characterCount(0)
            .build();
    }

    private UserCharacter createUserCharacter(User user, int level, String fileName) {
        return UserCharacter.builder()
            .user(user)
            .level(level)
            .fileName(fileName)
            .build();
    }

    private Friend createFriend(User requester, User receiver) {
        return Friend.builder()
            .requester(requester)
            .receiver(receiver)
            .build();
    }

    private LoginUserInfo createLoginUserInfo(int userId) {
        return LoginUserInfo.builder()
            .userId(userId)
            .build();
    }
}