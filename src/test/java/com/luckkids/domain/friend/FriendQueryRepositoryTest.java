package com.luckkids.domain.friend;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.luckkids.domain.user.*;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class FriendQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private FriendQueryRepository friendQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @DisplayName("친구 리스트를 조회한다.")
    @Test
    void getFriendList() {
        // given
        User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구");
        User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구");
        userRepository.saveAll(List.of(user1, user2));

        UserCharacter userCharacter1 = createUserCharacter(user1, "https://test.cloudfront.net/캐릭터1.json", "https://test.cloudfront.net/캐릭터1.png");
        UserCharacter userCharacter2 = createUserCharacter(user2, "https://test.cloudfront.net/캐릭터2.json", "https://test.cloudfront.net/캐릭터2.png");
        userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2));

        Friend friend = createFriend(user1, user2);
        friendRepository.save(friend);

        Pageable pageable = PageInfoServiceRequest.builder()
            .page(1)
            .size(10)
            .build()
            .toPageable();

        // when
        Page<FriendProfileDto> friendPagingList = friendQueryRepository.getFriendList(user1.getId(), pageable);

        // then
        List<FriendProfileDto> friendList = friendPagingList.getContent();
        assertThat(friendList)
            .extracting("friendId", "nickname", "luckPhrase", "imageFileUrl", "characterCount")
            .contains(
                tuple(user2.getId(), "테스트2", "테스트2의 행운문구", "https://test.cloudfront.net/캐릭터2.png", 0)
            );
    }

    private User createUser(String email, String password, String nickname, String luckPhrase) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(SnsType.NORMAL)
            .nickname(nickname)
            .luckPhrase(luckPhrase)
            .role(Role.USER)
            .settingStatus(SettingStatus.COMPLETE)
            .missionCount(0)
            .build();
    }

    private UserCharacter createUserCharacter(User user, String lottieFile, String imageFile) {
        return UserCharacter.builder()
            .user(user)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();
    }

    private Friend createFriend(User requester, User receiver) {
        return Friend.builder()
            .requester(requester)
            .receiver(receiver)
            .build();
    }
}