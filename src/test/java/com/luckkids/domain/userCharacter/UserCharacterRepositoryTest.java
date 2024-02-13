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
        LuckkidsCharacter luckkidsCharacter = createCharacter(CharacterType.SUN);
        luckkidsCharacterRepository.save(luckkidsCharacter);

        UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter);

        UserCharacter savedUserCharacter = userCharacterRepository.save(userCharacter);
        userCharacterRepository.deleteAllByUserId(user.getId());

        Optional<UserCharacter> findUserCharacter = userCharacterRepository.findById(savedUserCharacter.getId());

        assertThat(findUserCharacter.isEmpty()).isTrue();
    }

    private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter){
        return UserCharacter.builder()
            .user(user)
            .luckkidsCharacter(luckkidsCharacter)
            .characterProgressStatus(CharacterProgressStatus.IN_PROGRESS)
            .build();
    }

    private LuckkidsCharacter createCharacter(CharacterType characterType) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(1)
            .lottieFile("test.json")
            .imageFile("test.png")
            .build();
    }
}
