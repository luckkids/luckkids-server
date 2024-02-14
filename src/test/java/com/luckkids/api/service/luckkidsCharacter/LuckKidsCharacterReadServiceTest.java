package com.luckkids.api.service.luckkidsCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.luckkidsCharacter.response.LuckCharacterRandResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LuckKidsCharacterReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private LuckkidsCharacterReadService luckkidsCharacterReadService;

    @Autowired
    private LuckkidsCharacterRepository luckkidsCharacterRepository;



    @AfterEach
    void tearDown() {
        luckkidsCharacterRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("초기캐릭터를 랜덤 조회한다.")
    void findRandomCharacterLevel1() {
        //given
        settingCharacter();
        //when
        LuckCharacterRandResponse luckCharacterRandResponse = luckkidsCharacterReadService.findRandomCharacterLevel1();

        //then
        assertThat(luckCharacterRandResponse.getCharacterType()).isNotNull();
        assertThat(luckCharacterRandResponse.getLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("초기캐릭터 Entity를 랜덤 조회한다.")
    void findRandomLuckkidsCharacterLevel1() {
        //given
        settingCharacter();
        //when
        LuckkidsCharacter luckkidsCharacter = luckkidsCharacterReadService.findRandomLuckkidsCharacterLevel1();

        //then
        assertThat(luckkidsCharacter.getCharacterType()).isNotNull();
        assertThat(luckkidsCharacter.getLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("초기캐릭터 Entity 랜덤 조회 시 캐릭터가 없을 시 예외가 발생한다.")
    void findRandomLuckkidsCharacterLevel1ThrowException() {
        //when
        //then
        assertThatThrownBy(() -> luckkidsCharacterReadService.findRandomLuckkidsCharacterLevel1())
            .hasMessage("럭키즈 캐릭터가 존재하지않습니다.")
            .isInstanceOf(LuckKidsException.class);
    }

    @Test
    @DisplayName("초기캐릭터 Entity를 id로 조회한다.")
    void findById() {
        //given
        LuckkidsCharacter luckkidsCharacter = createCharacter(CharacterType.SUN);
        luckkidsCharacterRepository.save(luckkidsCharacter);

        //when
        LuckkidsCharacter findLuckKidsCharacter = luckkidsCharacterReadService.findById(luckkidsCharacter.getId());

        //then
        assertThat(findLuckKidsCharacter).extracting("id", "characterType", "level", "lottieFile", "imageFile")
            .contains(luckkidsCharacter.getId(), luckkidsCharacter.getCharacterType(), luckkidsCharacter.getLevel(), luckkidsCharacter.getLottieFile(), luckkidsCharacter.getImageFile());
    }

    @Test
    @DisplayName("초기캐릭터 Entity를 id로 조회시 데이터가 존재하지 않으면 예외가 발생한다.")
    void findByIdThrowException() {
        //when
        //then
        assertThatThrownBy(() -> luckkidsCharacterReadService.findById(1))
            .hasMessage("럭키즈 캐릭터가 존재하지않습니다.")
            .isInstanceOf(LuckKidsException.class);
    }

    private LuckkidsCharacter createCharacter(CharacterType characterType) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(1)
            .lottieFile("test.json")
            .imageFile("test.png")
            .build();
    }

    private LuckkidsCharacter createCharacterLevel2(CharacterType characterType) {
        return LuckkidsCharacter.builder()
            .characterType(characterType)
            .level(2)
            .lottieFile("test.json")
            .imageFile("test.png")
            .build();
    }
    private void settingCharacter() {
        CharacterType[] characterTypes = CharacterType.values();

        for(CharacterType characterType : characterTypes){
            luckkidsCharacterRepository.save(createCharacter(characterType));
        }

        for(CharacterType characterType : characterTypes){
            luckkidsCharacterRepository.save(createCharacterLevel2(characterType));
        }
    }
}
