package com.luckkids.api.service.initialSetting.response;

import com.luckkids.api.service.initialSetting.request.InitialSettingCharacterServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterResponse {

    private int id;
    private String characterName;
    private String fileName;

    @Builder
    private InitialSettingCharacterResponse(int id, String characterName, String fileName) {
        this.id = id;
        this.characterName = characterName;
        this.fileName = fileName;
    }
}
