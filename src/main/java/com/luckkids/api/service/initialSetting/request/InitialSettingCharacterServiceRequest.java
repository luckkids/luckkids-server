package com.luckkids.api.service.initialSetting.request;

import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import com.luckkids.api.service.userCharacter.response.UserCharacterCreateResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterServiceRequest {
    private String characterName;
    private String fileName;

    @Builder
    private InitialSettingCharacterServiceRequest(String characterName, String fileName) {
        this.characterName = characterName;
        this.fileName = fileName;
    }

    public UserCharacterCreateServiceRequest toCharacterServiceRequest(){
        return UserCharacterCreateServiceRequest.builder()
            .fileName(fileName)
            .characterName(characterName)
            .build();
    }
}
