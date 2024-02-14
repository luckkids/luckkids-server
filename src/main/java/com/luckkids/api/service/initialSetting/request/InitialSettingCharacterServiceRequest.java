package com.luckkids.api.service.initialSetting.request;

import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterServiceRequest {

    private int id;
    private String nickName;

    @Builder
    private InitialSettingCharacterServiceRequest(int id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public UserCharacterCreateServiceRequest toCharacterServiceRequest(){
        return UserCharacterCreateServiceRequest.builder()
            .id(id)
            .nickName(nickName)
            .build();
    }
}
