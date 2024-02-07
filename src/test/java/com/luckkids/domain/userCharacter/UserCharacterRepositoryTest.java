package com.luckkids.domain.userCharacter;

import com.luckkids.IntegrationTestSupport;
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
}
