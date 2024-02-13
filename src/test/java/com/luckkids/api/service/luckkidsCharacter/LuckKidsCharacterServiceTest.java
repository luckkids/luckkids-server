package com.luckkids.api.service.luckkidsCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.luckkidsCharacter.response.LuckCharacterRandResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class LuckKidsCharacterServiceTest extends IntegrationTestSupport {

    @Autowired
    private LuckkidsCharacterReadService luckkidsCharacterReadService;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;

    @BeforeEach
    void settingCharacter() {
        CharacterType[] characterTypes = CharacterType.values();

        for(CharacterType characterType : characterTypes){
            luckkidsCharacterRepository.save(createCharacter(characterType));
        }

        for(CharacterType characterType : characterTypes){
            luckkidsCharacterRepository.save(createCharacterLevel2(characterType));
        }
    }

    @AfterEach
    void tearDown() {
        luckkidsCharacterRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("초기캐릭터를 랜덤 조회한다.")
    void findRandomLevel1() {
        //when
        LuckCharacterRandResponse luckCharacterRandResponse = luckkidsCharacterReadService.findRandomCharacterLevel1();

        //then
        assertThat(luckCharacterRandResponse.getCharacterType()).isNotNull();
        assertThat(luckCharacterRandResponse.getLevel()).isEqualTo(1);
    }

    LuckkidsCharacter createCharacter(CharacterType characterType) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(1)
            .lottieFile("test.json")
            .imageFile("test.png")
            .build();
    }

    LuckkidsCharacter createCharacterLevel2(CharacterType characterType) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(2)
            .lottieFile("test.json")
            .imageFile("test.png")
            .build();
    }

}
