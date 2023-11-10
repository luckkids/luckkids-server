package com.luckkids.api.service.initialSetting.request;

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
}
