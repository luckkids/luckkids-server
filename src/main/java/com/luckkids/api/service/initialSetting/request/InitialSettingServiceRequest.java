package com.luckkids.api.service.initialSetting.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InitialSettingServiceRequest {
    private InitialSettingAlertServiceRequest alertSetting;
    private InitialSettingCharacterServiceRequest character;
    private List<InitialSettingMissionServiceRequest> missions;

    @Builder
    private InitialSettingServiceRequest(InitialSettingAlertServiceRequest alertSetting, InitialSettingCharacterServiceRequest character, List<InitialSettingMissionServiceRequest> missions) {
        this.alertSetting = alertSetting;
        this.character = character;
        this.missions = missions;
    }
}
