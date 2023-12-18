package com.luckkids.api.service.initialCharacter;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.initialCharacter.response.InitialCharacterRandResponse;
import com.luckkids.domain.initialCharacter.InitialCharacter;
import com.luckkids.domain.initialCharacter.InitialCharacterRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class InitialCharacterServiceTest extends IntegrationTestSupport {

    @Autowired
    private InitialCharacterService initialCharacterService;

    @Autowired
    private InitialCharacterRepository initialCharacterRepository;

    @BeforeEach
    void settingCharacter(){
        IntStream.rangeClosed(1, 4).forEach(i -> {
            InitialCharacter character = createInitialCharacter(i);
            initialCharacterRepository.save(character);
        });
    }

    @AfterEach
    void tearDown() {
        initialCharacterRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("초기캐릭터 파일정보를 모두 조회한다.")
    void findAll(){
        List<InitialCharacterRandResponse> initialCharacterRandResponses = initialCharacterService.findAll();

        assertThat(initialCharacterRandResponses).hasSize(4)
            .extracting("characterName", "fileName", "fileUrl")
            .containsExactlyInAnyOrder(
                tuple("테스트1", "test1.json", "https://d1i0as5mndfs61.cloudfront.net/test1.json"),
                tuple("테스트2", "test2.json", "https://d1i0as5mndfs61.cloudfront.net/test2.json"),
                tuple("테스트3", "test3.json", "https://d1i0as5mndfs61.cloudfront.net/test3.json"),
                tuple("테스트4", "test4.json", "https://d1i0as5mndfs61.cloudfront.net/test4.json")
            );
    }

    InitialCharacter createInitialCharacter(int i){
        return InitialCharacter.builder()
            .characterName("테스트"+i)
            .fileName("test"+i+".json")
            .build();
    }

}
