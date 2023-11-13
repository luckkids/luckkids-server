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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;

public class FriendReadServiceTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    UserCharacterRepository userCharacterRepository;
    @Autowired
    FriendReadService friendReadService;
    @Autowired
    SecurityService securityService;

    @AfterEach
    void tearDown() {
        userCharacterRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }


    @DisplayName("친구의 프로필을 조회한다.")
    @Test
    void readProfile(){
        User user1 = createUser(1);
        User user2 = createUser(2);

        createFriend(user1, user2);

        FriendProfileReadResponse response = friendReadService.readProfile(user2.getId());

        assertThat(response)
            .extracting("phraseDescription", "fileUrl", "characterName", "level")
            .containsExactlyInAnyOrder(
                "행운입니다.", "file2", "character2", 2
            );
    }

    @DisplayName("존재하지않는 사용자친구의 프로필을 조회시 예외를 발생시킨다.")
    @Test
    void readProfileWithoutFriend(){
        User user1 = createUser(1);
        User user2 = createUser(2);

        createFriend(user1, user2);

        assertThatThrownBy(() -> friendReadService.readProfile(3))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("친구가 존재하지 않습니다.");
    }

    @DisplayName("사용자의 친구 랭킹리스트를 조회한다.")
    @Test
    void readListFriend(){
        User user1 = createUser(1);
        User user2 = createUser(2);
        User user3 = createUser(3);

        createFriend(user1, user2);
        createFriend(user1, user3);
        createFriend(user2, user3);

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user1.getId()));

        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

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
                1,1,2L
            );
    }

    @DisplayName("사용자의 친구가 없을 시 빈리스트를 반환한다.")
    @Test
    void readListWithoutFriend(){
        User user1 = createUser(1);
        User user2 = createUser(2);
        User user3 = createUser(3);
        User user4 = createUser(4);

        createFriend(user1, user2);
        createFriend(user1, user3);
        createFriend(user2, user3);

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(user4.getId()));

        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

        List<FriendListReadResponse> responseList = response.getContent();
        PageableCustom pageableCustom = response.getPageInfo();

        assertThat(responseList).hasSize(0);

        assertThat(pageableCustom)
            .extracting("currentPage", "totalPages", "totalElements")
            .containsExactlyInAnyOrder(
                1,0,0L
            );
    }

    @DisplayName("사용자의 친구가 많을 시 페이징 처리를 한다. 첫번째 페이지")
    @Test
    void readListFriendFirstPaging(){
        int userId = createManyUser();

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(userId));

        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

        List<FriendListReadResponse> responseList = response.getContent();
        PageableCustom pageableCustom = response.getPageInfo();

        // Clover갯수순으로 내림차순 정렬
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
                1,2,12L
            );
    }

    @DisplayName("사용자의 친구가 많을 시 페이징 처리를 한다. 두번째 페이지")
    @Test
    void readListFriendSecondPaging(){
        int userId = createManyUser();

        PageInfoServiceRequest pageDto = PageInfoServiceRequest.builder()
            .page(2)
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo(userId));

        PageCustom<FriendListReadResponse> response = friendReadService.readListFriend(pageDto);

        List<FriendListReadResponse> responseList = response.getContent();
        PageableCustom pageableCustom = response.getPageInfo();

        // Clover갯수순으로 내림차순 정렬
        assertThat(responseList).hasSize(2)
            .extracting("characterName", "fileUrl", "missionCount")
            .containsExactlyInAnyOrder(
                tuple("character3", "file3", 3),
                tuple("character2", "file2", 2)
            );

        assertThat(pageableCustom)
            .extracting("currentPage", "totalPages", "totalElements")
            .containsExactlyInAnyOrder(
                2,2,12L
            );
    }

    void createFriend(User requester, User receiver){
        Friend friend = Friend.builder()
            .requester(requester)
            .receiver(receiver)
            .friendStatus(FriendStatus.ACCEPTED)
            .build();

        friendRepository.save(friend);
    }

    User createUser(int i){
        User user = User.builder()
            .email("test"+i)
            .password("1234")
            .missionCount(i)
            .luckPharase("행운입니다.")
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .build();

        UserCharacter userCharacter = UserCharacter.builder()
            .characterName("character"+i)
            .fileName("file"+i)
            .level(i)
            .build();

        user.changeUserCharacter(userCharacter);

        return userRepository.save(user);
    }

    int createManyUser(){
        User user1 = createUser(1);
        for(int i=2; i<=13; i++){
            User user = createUser(i);
            createFriend(user1, user);
        }
        return user1.getId();
    }

    private UserInfo createUserInfo(int userId) {
        return UserInfo.builder()
            .userId(userId)
            .email("")
            .build();
    }
}
