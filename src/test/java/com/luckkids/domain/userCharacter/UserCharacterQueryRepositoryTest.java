package com.luckkids.domain.userCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.projection.UserCharacterSummaryDto;
import com.luckkids.domain.userCharacter.projection.UserInProgressCharacterDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOUD;
import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOVER;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.COMPLETED;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class UserCharacterQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserCharacterQueryRepository userCharacterQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @DisplayName("진행중인 캐릭터를 가져온다.")
    @Test
    void findInProgressCharacterByUserId() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 20);
        LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
        UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter, IN_PROGRESS);

        userRepository.save(user);
        luckkidsCharacterRepository.save(luckkidsCharacter);
        userCharacterRepository.save(userCharacter);

        // when
        UserInProgressCharacterDto result = userCharacterQueryRepository.findInProgressCharacterByUserId(user.getId());

        // then
        assertThat(result.luckkidsCharacter().getId()).isEqualTo(luckkidsCharacter.getId());
        assertThat(result.userCharacter().getId()).isEqualTo(userCharacter.getId());
    }

    @DisplayName("지금까지 모은 캐릭터들을 가져온다.")
    @Test
    void findUserCharacterSummary() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 120);
        LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/test1.json", "https://test.cloudfront.net/test1.png");
        LuckkidsCharacter luckkidsCharacter5 = createLuckkidsCharacter(CLOUD, 5, "https://test.cloudfront.net/test5.json", "https://test.cloudfront.net/test5.png");
        UserCharacter userCharacter1 = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);
        UserCharacter userCharacter2 = createUserCharacter(user, luckkidsCharacter5, COMPLETED);

        userRepository.save(user);
        luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter5));
        userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2));

        // when
        List<UserCharacterSummaryDto> result = userCharacterQueryRepository.findUserCharacterSummary(user.getId());

        // then
        assertThat(result)
            .extracting("characterType", "level", "characterProgressStatus")
            .containsExactlyInAnyOrder(
                tuple(CLOVER, 1, IN_PROGRESS),
                tuple(CLOUD, 5, COMPLETED)
            );
    }

    private User createUser(String email, String password, SnsType snsType, int missionCount) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
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

    private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter, CharacterProgressStatus characterProgressStatus) {
        return UserCharacter.builder()
            .user(user)
            .luckkidsCharacter(luckkidsCharacter)
            .characterProgressStatus(characterProgressStatus)
            .build();
    }

}