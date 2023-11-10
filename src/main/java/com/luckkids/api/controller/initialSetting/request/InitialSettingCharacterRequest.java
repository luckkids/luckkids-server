package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingCharacterServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterRequest {
    private String characterName;
    private String fileName;

    @Builder
    private InitialSettingCharacterRequest(String characterName, String fileName) {
        this.characterName = characterName;
        this.fileName = fileName;
    }

    public InitialSettingCharacterServiceRequest toServiceRequest(){
        return InitialSettingCharacterServiceRequest.builder()
            .characterName(characterName)
            .fileName(fileName)
            .build();
    }
}
