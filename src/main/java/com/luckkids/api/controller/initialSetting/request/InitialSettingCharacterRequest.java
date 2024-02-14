package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingCharacterServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterRequest {

    private int id;
    @NotBlank(message = "럭키즈 닉네임은 필수입니다.")
    private String nickName;

    @Builder
    private InitialSettingCharacterRequest(int id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public InitialSettingCharacterServiceRequest toServiceRequest(){
        return InitialSettingCharacterServiceRequest.builder()
            .id(id)
            .nickName(nickName)
            .build();
    }
}
