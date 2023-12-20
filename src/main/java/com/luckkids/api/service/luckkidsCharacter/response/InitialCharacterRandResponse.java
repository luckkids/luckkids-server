package com.luckkids.api.service.luckkidsCharacter.response;

import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialCharacterRandResponse {
    private String characterName;
    private String fileName;
    private String fileUrl;

    @Builder
    private InitialCharacterRandResponse(String characterName, String fileName, String fileUrl) {
        this.characterName = characterName;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public static InitialCharacterRandResponse of(LuckkidsCharacter character, String cloudFrontUrl){
        return InitialCharacterRandResponse.builder()
            .characterName(character.getCharacterId().getCharacterName())
            .fileName(character.getFileName())
            .fileUrl(cloudFrontUrl+character.getFileName())
            .build();
    }
}
