package com.luckkids.api.service.initialSetting.request;

import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterServiceRequest {
    private String characterNickname;
    private String fileName;

    @Builder
    private InitialSettingCharacterServiceRequest(String characterNickname, String fileName) {
        this.characterNickname = characterNickname;
        this.fileName = fileName;
    }

    public UserCharacterCreateServiceRequest toCharacterServiceRequest(){
        return UserCharacterCreateServiceRequest.builder()
            .characterNickname(characterNickname)
            .fileName(fileName)
            .build();
    }
}
