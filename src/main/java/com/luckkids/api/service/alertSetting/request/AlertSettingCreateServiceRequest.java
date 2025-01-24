package com.luckkids.api.service.alertSetting.request;

import com.luckkids.mission.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingCreateServiceRequest {

    private String deviceId;
    private AlertStatus alertStatus;

    @Builder
    private AlertSettingCreateServiceRequest(String deviceId, AlertStatus alertStatus) {
        this.deviceId = deviceId;
        this.alertStatus = alertStatus;
    }


}
