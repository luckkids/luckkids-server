package com.luckkids.domain.luckkidsCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.luckkidsCharacter.projection.LuckkidsCharacterDto;
import com.luckkids.domain.user.*;
import com.luckkids.domain.userCharacter.CharacterProgressStatus;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOVER;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class LuckkidsCharacterQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private LuckkidsCharacterQueryRepository luckkidsCharacterQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @DisplayName("특정 레벨의 럭키즈 캐릭터를 가져온다.")
    @Test
    void findCharacterByLevel() {
        // given
        User user = createUser();
        UserCharacter userCharacter = createUserCharacter(user, IN_PROGRESS, "https://test.cloudfront.net/test0.json", "https://test.cloudfront.net/test0.png");
        LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 0, "https://test.cloudfront.net/test0.json", "https://test.cloudfront.net/test0.png");
        LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");

        userRepository.save(user);
        userCharacterRepository.save(userCharacter);
        luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));

        // when
        LuckkidsCharacterDto result = luckkidsCharacterQueryRepository.findCharacterByLevel(1, user.getId());

        // then
        assertThat(result)
            .extracting("characterType", "lottieFile", "imageFile")
            .contains(CLOVER, "https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
    }

    private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level, String lottieFile, String imageFile) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(level)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();

    }

    private UserCharacter createUserCharacter(User user, CharacterProgressStatus characterProgressStatus, String lottieFile, String imageFile) {
        return UserCharacter.builder()
            .user(user)
            .characterProgressStatus(characterProgressStatus)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();
    }

    private User createUser() {
        return User.builder()
            .email("test@gmail.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .nickname("test")
            .luckPhrase("test 문구")
            .role(Role.USER)
            .settingStatus(SettingStatus.COMPLETE)
            .missionCount(0)
            .build();
    }
}