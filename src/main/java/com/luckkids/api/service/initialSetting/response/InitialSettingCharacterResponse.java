package com.luckkids.api.service.initialSetting.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterResponse {

    private int id;
    private String characterNickname;
    private String fileName;

    @Builder
    private InitialSettingCharacterResponse(int id, String characterNickname, String fileName) {
        this.id = id;
        this.characterNickname = characterNickname;
        this.fileName = fileName;
    }
}
