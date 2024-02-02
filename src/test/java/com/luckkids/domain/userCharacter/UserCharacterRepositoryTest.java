package com.luckkids.domain.userCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOVER;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class UserCharacterRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCharacterRepository userCharacterRepository;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @Test
    @DisplayName("사용자의 캐릭터를 삭제한다.")
    void deleteAllByUserId() {
        User user = User.builder()
            .email("test@email.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .build();

        userRepository.save(user);

        UserCharacter userCharacter = UserCharacter.builder()
//            .characterNickname("test")    ⭐️ private create 함수는 만드는게 좋을 것 같아요!
//            .file("https://test.cloudfront.net/test.json")
//            .level(1)
//            .user(user)
            .build();

        UserCharacter savedUserCharacter = userCharacterRepository.save(userCharacter);
        userCharacterRepository.deleteAllByUserId(user.getId());

        Optional<UserCharacter> findUserCharacter = userCharacterRepository.findById(savedUserCharacter.getId());

        assertThat(findUserCharacter.isEmpty()).isTrue();
    }

    @DisplayName("캐릭터 상태에 맞는 캐릭터를 가져온다.")
    @Test
    void findByCharacterProgressStatus() {
        // given
        User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 20);
        LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 0, "https://test.cloudfront.net/test0.json", "https://test.cloudfront.net/test0.png");
        UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter, IN_PROGRESS);

        userRepository.save(user);
        luckkidsCharacterRepository.save(luckkidsCharacter);
        userCharacterRepository.save(userCharacter);

        // when
        UserCharacter findUserCharacter = userCharacterRepository.findByCharacterProgressStatus(IN_PROGRESS);

        // then
        assertThat(findUserCharacter.getId()).isEqualTo(userCharacter.getId());
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
