package com.luckkids.domain.luckkidsCharacter;

import com.luckkids.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.CLOVER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class LuckkidsCharacterRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @DisplayName("캐릭터 타입과 레벨에 맞는 럭키즈 캐릭터를 가져온다.")
    @Test
    void findByCharacterTypeAndLevel() {
        // given
        LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1, "https://test.cloudfront.net/test0.json", "https://test.cloudfront.net/test0.png");
        luckkidsCharacterRepository.save(luckkidsCharacter);

        // when
        LuckkidsCharacter findLuckkidsCharacter = luckkidsCharacterRepository.findByCharacterTypeAndLevel(CLOVER, 1);

        // then
        assertThat(findLuckkidsCharacter)
            .extracting("characterType", "level", "lottieFile", "imageFile")
            .contains(CLOVER, 1, "https://test.cloudfront.net/test0.json", "https://test.cloudfront.net/test0.png");

    }

    private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level, String lottieFile, String imageFile) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(level)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();

    }
}