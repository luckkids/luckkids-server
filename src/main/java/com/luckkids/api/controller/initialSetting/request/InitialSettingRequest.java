package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingServiceRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingRequest {
    private InitialSettingAlertRequest alertSetting;
    private InitialSettingCharacterRequest character;
    private InitialSettingMissionRequest mission;

    public InitialSettingServiceRequest toServiceRequest(){
        return InitialSettingServiceRequest.builder()
            .alertSetting(alertSetting.toServiceRequest())
            .character(character.toServiceRequest())
            .mission(mission.toServiceRequest())
            .build();
    }
}
