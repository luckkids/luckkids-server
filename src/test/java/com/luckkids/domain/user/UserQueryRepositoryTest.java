package com.luckkids.domain.user;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.projection.MyProfileDto;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOVER;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @DisplayName("나의 프로필을 조회한다.")
    @Test
    void getMyProfile() {
        // given
        User user = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구");
        userRepository.save(user);

        LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/캐릭터1.json", "https://test.cloudfront.net/캐릭터1.png");
        luckkidsCharacterRepository.save(luckkidsCharacter);
        
        UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter);
        userCharacterRepository.save(userCharacter);

        // when
        MyProfileDto myProfile = userQueryRepository.getMyProfile(user.getId());

        // then
        assertThat(myProfile)
            .extracting("myId", "nickname", "luckPhrase", "imageFileUrl", "characterCount")
            .contains(
                user.getId(), "테스트1", "테스트1의 행운문구", "https://test.cloudfront.net/캐릭터1.png", 0
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
}