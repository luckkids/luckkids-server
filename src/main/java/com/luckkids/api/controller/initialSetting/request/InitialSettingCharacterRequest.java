package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingCharacterServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterRequest {

    @NotBlank(message = "캐릭터 닉네임은 필수입니다.")
    private String characterNickname;
    @NotBlank(message = "파일명은 필수입니다.")
    private String fileName;

    @Builder
    private InitialSettingCharacterRequest(String characterNickname, String fileName) {
        this.characterNickname = characterNickname;
        this.fileName = fileName;
    }

    public InitialSettingCharacterServiceRequest toServiceRequest(){
        return InitialSettingCharacterServiceRequest.builder()
            .characterNickname(characterNickname)
            .build();
    }
}
