package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingServiceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class InitialSettingRequest {

    @Valid @NotNull(message = "알림설정 요청값은 필수입니다.")
    private InitialSettingAlertRequest alertSetting;
    @Valid @NotNull(message = "캐릭터설정 요청값은 필수입니다.")
    private InitialSettingCharacterRequest character;
    @Valid @NotNull(message = "미션설정 요청값은 필수입니다.")
    private List<InitialSettingMissionRequest> missions;

    @Builder
    private InitialSettingRequest(InitialSettingAlertRequest alertSetting, InitialSettingCharacterRequest character, List<InitialSettingMissionRequest> missions) {
        this.alertSetting = alertSetting;
        this.character = character;
        this.missions = missions;
    }

    public InitialSettingServiceRequest toServiceRequest(){
        return InitialSettingServiceRequest.builder()
            .alertSetting(alertSetting.toServiceRequest())
            .character(character.toServiceRequest())
            .missions(missions.stream().map(InitialSettingMissionRequest::toServiceRequest).collect(Collectors.toList()))
            .build();
    }
}
