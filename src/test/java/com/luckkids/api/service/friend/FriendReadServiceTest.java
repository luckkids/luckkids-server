package com.luckkids.api.service.friend;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import com.luckkids.api.service.response.PageCustom;
import com.luckkids.api.service.response.PageableCustom;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.friends.Friend;
import com.luckkids.domain.friends.FriendRepository;
import com.luckkids.domain.friends.FriendStatus;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;

public class FriendReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private FriendReadService friendReadService;

    @Autowired
    private SecurityService securityService;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("친구의 프로필을 조회한다.")
    @Test
    void readProfile() {
        // given
        List<User> users = new ArrayList<>();
        List<UserCharacter> userCharacters = new ArrayList<>();

        IntStream.rangeClosed(1, 2).forEach(i -> {
            User user = createUser(i);
            UserCharacter userCharacter = createUserCharacter(user, i);

            users.add(user);
            userCharacters.add(userCharacter);
        });

        Friend friend = createFriend(users.get(0), users.get(1));

        userRepository.saveAll(users);
        userCharacterRepository.saveAll(userCharacters);
        friendRepository.save(friend);

        // when
        FriendProfileReadResponse response = friendReadService.readProfile(users.get(1).getId());

        // then
        assertThat(response)
            .extracting("phraseDescription", "fileUrl", "characterName", "level")
            .containsExactlyInAnyOrder(
                "행운입니다.", "file2", "character2", 2
            );
    }

    @DisplayName("존재하지않는 사용자친구의 프로필을 조회시 예외를 발생시킨다.")
    @Test
    void readProfileWithoutFriend() {
        // given
        List<User> users = new ArrayList<>();
        List<UserCharacter> userCharacters = new ArrayList<>();

        IntStream.rangeClosed(1, 2).forEach(i -> {
            User user = createUser(i);
            UserCharacter userCharacter = createUserCharacter(user, i);

            users.add(user);
            userCharacters.add(userCharacter);
        });

        Friend friend = createFriend(users.get(0), users.get(1));

        userRepository.saveAll(users);
        userCharacterRepository.saveAll(userCharacters);
        friendRepository.save(friend);

        // when // then
        assertThatThrownBy(() -> friendReadService.readProfile(3))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("친구가 존재하지 않습니다.");
    }

    @DisplayName("사용자의 친구 랭킹리스트를 조회한다.")
    @Test
    void readListFriend() {
        // given
        List<User> users = new ArrayList<>();
        List<UserCharacter> userCharacters = new ArrayList<>();
        List<Friend> friends = new ArrayList<>();

        IntStream.rangeClosed(1, 3).forEach(i -> {
            User user = createUser(i);
            UserCharacter userCharacter = createUserCharacter(user, i);

            users.add(user);
            userCharacters.add(userCharacter);
        });

        friends.add(createFriend(users.get(0), users.get(1)));
        friends.add(createFriend(users.get(0), users.get(2)));
        friends.add(createFriend(users.get(1), users.get(2)));

        userRepository.saveAll(users);
        userCharacterRepository.saveAll(userCharacters);
        friendRepository.saveAll(friends);

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(users.get(0).getId()));

        // when
        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

        // then
        List<FriendListReadResponse> responseList = response.getContent();
        PageableCustom pageableCustom = response.getPageInfo();

        assertThat(responseList).hasSize(2)
            .extracting("characterName", "fileUrl", "missionCount")
            .containsExactlyInAnyOrder(
                tuple("character2", "file2", 2),
                tuple("character3", "file3", 3)
            );
        assertThat(pageableCustom)
            .extracting("currentPage", "totalPages", "totalElements")
            .containsExactlyInAnyOrder(
                1, 1, 2L
            );
    }

    @DisplayName("사용자의 친구가 없을 시 빈리스트를 반환한다.")
    @Test
    void readListWithoutFriend() {
        // given
        List<User> users = new ArrayList<>();
        List<UserCharacter> userCharacters = new ArrayList<>();
        List<Friend> friends = new ArrayList<>();

        IntStream.rangeClosed(1, 4).forEach(i -> {
            User user = createUser(i);
            UserCharacter userCharacter = createUserCharacter(user, i);

            users.add(user);
            userCharacters.add(userCharacter);
        });

        friends.add(createFriend(users.get(0), users.get(1)));
        friends.add(createFriend(users.get(0), users.get(2)));
        friends.add(createFriend(users.get(1), users.get(2)));

        userRepository.saveAll(users);
        userCharacterRepository.saveAll(userCharacters);
        friendRepository.saveAll(friends);

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(users.get(3).getId()));

        // when
        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

        // then
        List<FriendListReadResponse> responseList = response.getContent();
        PageableCustom pageableCustom = response.getPageInfo();

        assertThat(responseList).hasSize(0);

        assertThat(pageableCustom)
            .extracting("currentPage", "totalPages", "totalElements")
            .containsExactlyInAnyOrder(
                1, 0, 0L
            );
    }

    @DisplayName("사용자의 친구가 많을 시 페이징 처리를 한다. 첫번째 페이지")
    @Test
    void readListFriendFirstPaging() {
        // given
        List<User> users = new ArrayList<>();
        List<UserCharacter> userCharacters = new ArrayList<>();
        List<Friend> friends = new ArrayList<>();

        User user1 = createUser(1);
        users.add(user1);

        IntStream.rangeClosed(2, 13).forEach(i -> {
            User user = createUser(i);
            UserCharacter userCharacter = createUserCharacter(user, i);
            Friend friend = createFriend(user1, user);

            users.add(user);
            userCharacters.add(userCharacter);
            friends.add(friend);
        });

        userRepository.saveAll(users);
        userCharacterRepository.saveAll(userCharacters);
        friendRepository.saveAll(friends);

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user1.getId()));

        // when
        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

        // then
        List<FriendListReadResponse> responseList = response.getContent();
        PageableCustom pageableCustom = response.getPageInfo();

        assertThat(responseList).hasSize(10)
            .extracting("characterName", "fileUrl", "missionCount")
            .containsExactlyInAnyOrder(
                tuple("character13", "file13", 13),
                tuple("character12", "file12", 12),
                tuple("character11", "file11", 11),
                tuple("character10", "file10", 10),
                tuple("character9", "file9", 9),
                tuple("character8", "file8", 8),
                tuple("character7", "file7", 7),
                tuple("character6", "file6", 6),
                tuple("character5", "file5", 5),
                tuple("character4", "file4", 4)
            );

        assertThat(pageableCustom)
            .extracting("currentPage", "totalPages", "totalElements")
            .containsExactlyInAnyOrder(
                1, 2, 12L
            );
    }

    @DisplayName("사용자의 친구가 많을 시 페이징 처리를 한다. 두번째 페이지")
    @Test
    void readListFriendSecondPaging() {
        // given
        List<User> users = new ArrayList<>();
        List<UserCharacter> userCharacters = new ArrayList<>();
        List<Friend> friends = new ArrayList<>();

        User user1 = createUser(1);
        users.add(user1);

        IntStream.rangeClosed(2, 13).forEach(i -> {
            User user = createUser(i);
            UserCharacter userCharacter = createUserCharacter(user, i);
            Friend friend = createFriend(user1, user);

            users.add(user);
            userCharacters.add(userCharacter);
            friends.add(friend);
        });

        userRepository.saveAll(users);
        userCharacterRepository.saveAll(userCharacters);
        friendRepository.saveAll(friends);

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(2)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user1.getId()));

        // when
        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

        // then
        List<FriendListReadResponse> responseList = response.getContent();
        PageableCustom pageableCustom = response.getPageInfo();

        assertThat(responseList).hasSize(2)
            .extracting("characterName", "fileUrl", "missionCount")
            .containsExactlyInAnyOrder(
                tuple("character3", "file3", 3),
                tuple("character2", "file2", 2)
            );

        assertThat(pageableCustom)
            .extracting("currentPage", "totalPages", "totalElements")
            .containsExactlyInAnyOrder(
                2, 2, 12L
            );
    }

    private User createUser(int i) {
        return User.builder()
            .email("test" + i)
            .password("1234")
            .phoneNumber("01064091048")
            .missionCount(i)
            .luckPhrases("행운입니다.")
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .build();
    }

    private UserCharacter createUserCharacter(User user, int i) {
        return UserCharacter.builder()
            .user(user)
            .characterName("character" + i)
            .fileName("file" + i)
            .level(i)
            .build();
    }

    private Friend createFriend(User requester, User receiver) {
        return Friend.builder()
            .requester(requester)
            .receiver(receiver)
            .friendStatus(FriendStatus.ACCEPTED)
            .build();
    }

    private UserInfo createUserInfo(int userId) {
        return UserInfo.builder()
            .userId(userId)
            .email("")
            .build();
    }
}