package com.luckkids.api.service.initialSetting.response;

import com.luckkids.api.service.initialSetting.request.InitialSettingAlertServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingAlertResponse {
    private AlertStatus entire;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus notice;

    @Builder
    private InitialSettingAlertResponse(AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus notice) {
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.notice = notice;
    }
}
