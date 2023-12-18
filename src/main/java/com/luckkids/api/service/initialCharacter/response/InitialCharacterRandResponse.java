package com.luckkids.api.service.initialCharacter.response;

import com.luckkids.domain.initialCharacter.InitialCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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

    public static InitialCharacterRandResponse of(InitialCharacter initialCharacter, String cloudFrontUrl){
        return InitialCharacterRandResponse.builder()
            .characterName(initialCharacter.getCharacterName())
            .fileName(initialCharacter.getFileName())
            .fileUrl(cloudFrontUrl+initialCharacter.getFileName())
            .build();
    }
}
