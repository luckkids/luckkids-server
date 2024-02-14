package com.luckkids.api.service.friend;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.page.response.PageableCustom;
import com.luckkids.api.service.friend.response.FriendListResponse;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
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

import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOVER;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
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

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        luckkidsCharacterRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자의 친구 리스트를 조회한다.")
    @Test
    void getFriendList() {
        // given
        User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 100);
        User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 151);
        User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 200);
        userRepository.saveAll(List.of(user1, user2, user3));

        LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/캐릭터1.json", "https://test.cloudfront.net/캐릭터1.png");
        LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 2, "https://test.cloudfront.net/캐릭터2.json", "https://test.cloudfront.net/캐릭터2.png");
        luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));

        UserCharacter userCharacter1 = createUserCharacter(user1, luckkidsCharacter1);
        UserCharacter userCharacter2 = createUserCharacter(user2, luckkidsCharacter2);
        UserCharacter userCharacter3 = createUserCharacter(user3, luckkidsCharacter2);
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
            .extracting("myId", "nickname", "luckPhrase", "imageFileUrl", "characterCount")
            .containsExactlyInAnyOrder(
                user1.getId(), "테스트1", "테스트1의 행운문구", "https://test.cloudfront.net/캐릭터1.png", 1
            );

        PageCustom<FriendProfileDto> friendPagingList = response.getFriendList();

        List<FriendProfileDto> friendList = friendPagingList.getContent();
        assertThat(friendList)
            .extracting("friendId", "nickname", "luckPhrase", "imageFileUrl", "characterCount")
            .containsExactlyInAnyOrder(
                tuple(user2.getId(), "테스트2", "테스트2의 행운문구", "https://test.cloudfront.net/캐릭터2.png", 1),
                tuple(user3.getId(), "테스트3", "테스트3의 행운문구", "https://test.cloudfront.net/캐릭터2.png", 2)
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
    void getFriendListWithoutFriend() {
        // given
        User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
        User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 0);
        User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 0);
        userRepository.saveAll(List.of(user1, user2, user3));

        LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/캐릭터1.json", "https://test.cloudfront.net/캐릭터1.png");
        LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 2, "https://test.cloudfront.net/캐릭터2.json", "https://test.cloudfront.net/캐릭터2.png");
        luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));

        UserCharacter userCharacter1 = createUserCharacter(user1, luckkidsCharacter1);
        UserCharacter userCharacter2 = createUserCharacter(user2, luckkidsCharacter2);
        UserCharacter userCharacter3 = createUserCharacter(user3, luckkidsCharacter2);
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
            .extracting("myId", "nickname", "luckPhrase", "imageFileUrl", "characterCount")
            .contains(
                user2.getId(), "테스트2", "테스트2의 행운문구", "https://test.cloudfront.net/캐릭터2.png", 0
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

    private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level, String lottieFile, String imageFile) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(level)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();

    }

    private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter) {
        return UserCharacter.builder()
            .user(user)
            .luckkidsCharacter(luckkidsCharacter)
            .characterProgressStatus(IN_PROGRESS)
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