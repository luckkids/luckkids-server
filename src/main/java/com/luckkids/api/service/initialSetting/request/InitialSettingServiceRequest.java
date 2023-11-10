package com.luckkids.api.service.initialSetting.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingServiceRequest {
    private InitialSettingAlertServiceRequest alertSetting;
    private InitialSettingCharacterServiceRequest character;
    private InitialSettingMissionServiceRequest mission;

    @Builder
    private InitialSettingServiceRequest(InitialSettingAlertServiceRequest alertSetting, InitialSettingCharacterServiceRequest character, InitialSettingMissionServiceRequest mission) {
        this.alertSetting = alertSetting;
        this.character = character;
        this.mission = mission;
    }
}
