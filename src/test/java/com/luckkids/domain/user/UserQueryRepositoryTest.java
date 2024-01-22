package com.luckkids.domain.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.projection.MyProfileDto;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @DisplayName("나의 프로필을 조회한다.")
    @Test
    void getMyProfile() {
        // given
        User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구");
        userRepository.save(user);

        UserCharacter userCharacter = createUserCharacter(user, 1, "캐릭터1.json");
        userCharacterRepository.save(userCharacter);

        // when
        MyProfileDto myProfile = userQueryRepository.getMyProfile(user.getId());

        // then
        assertThat(myProfile)
            .extracting("myId", "nickname", "luckPhrases", "fileUrl", "characterCount")
            .contains(
                user.getId(), "테스트1", "테스트1의 행운문구", "캐릭터1.json", 0
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
            .file(fileName)
            .build();
    }
}