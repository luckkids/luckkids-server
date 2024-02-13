package com.luckkids.api.service.initialSetting.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterResponse {

    private int id;
    private String lottieFile;
    private String nickName;

    @Builder
    private InitialSettingCharacterResponse(int id, String lottieFile, String nickName) {
        this.id = id;
        this.lottieFile = lottieFile;
        this.nickName = nickName;
    }
}
