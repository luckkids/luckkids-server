package com.luckkids.api.service.initialSetting.response;

import com.luckkids.api.controller.initialSetting.request.InitialSettingAlertRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingCharacterRequest;
import com.luckkids.api.controller.initialSetting.request.InitialSettingMissionRequest;
import com.luckkids.api.service.initialSetting.request.InitialSettingServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InitialSettingResponse {
    private InitialSettingAlertResponse alertSetting;
    private InitialSettingCharacterResponse character;
    private List<InitialSettingMissionResponse> missions;

    @Builder
    private InitialSettingResponse(InitialSettingAlertResponse alertSetting, InitialSettingCharacterResponse character, List<InitialSettingMissionResponse> missions) {
        this.alertSetting = alertSetting;
        this.character = character;
        this.missions = missions;
    }

    public static InitialSettingResponse of(InitialSettingAlertResponse alertSetting, InitialSettingCharacterResponse character, List<InitialSettingMissionResponse> missions){
        return InitialSettingResponse.builder()
            .alertSetting(alertSetting)
            .character(character)
            .missions(missions)
            .build();
    }
}
