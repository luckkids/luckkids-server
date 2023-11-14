package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingCharacterServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterRequest {

    @NotBlank(message = "캐릭터 이름은 필수입니다.")
    private String characterName;
    @NotBlank(message = "파일명은 필수입니다.")
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
