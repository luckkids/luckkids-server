package com.luckkids.api.service.userCharacter.response;

import com.luckkids.api.service.initialSetting.response.InitialSettingCharacterResponse;
import com.luckkids.domain.userCharacter.UserCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterCreateResponse {

    private int id;
    private String fileName;
    private String characterName;

    @Builder
    private UserCharacterCreateResponse(int id, String fileName, String characterName) {
        this.id = id;
        this.fileName = fileName;
        this.characterName = characterName;
    }

    public static UserCharacterCreateResponse of(UserCharacter userCharacter){
        return UserCharacterCreateResponse.builder()
            .id(userCharacter.getId())
            .fileName(userCharacter.getFileName())
            .characterName(userCharacter.getCharacterName())
            .build();
    }

    public InitialSettingCharacterResponse toInitialSettingResponse(){
        return InitialSettingCharacterResponse.builder()
            .fileName(fileName)
            .characterName(characterName)
            .build();
    }

}
