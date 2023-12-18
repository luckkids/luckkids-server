package com.luckkids.api.service.initialCharacter.response;

import com.luckkids.domain.initialCharacter.InitialCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialCharacterRandResponse {
    private String characterName;
    private String fileName;

    @Builder
    private InitialCharacterRandResponse(String characterName, String fileName) {
        this.characterName = characterName;
        this.fileName = fileName;
    }

    public static InitialCharacterRandResponse of(InitialCharacter initialCharacter){
        return InitialCharacterRandResponse.builder()
            .characterName(initialCharacter.getCharacterName())
            .fileName(initialCharacter.getFileName())
            .build();
    }
}
