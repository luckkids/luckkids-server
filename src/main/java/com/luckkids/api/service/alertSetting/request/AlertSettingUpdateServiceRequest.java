package com.luckkids.api.service.alertSetting.request;

import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.mission.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingUpdateServiceRequest {
    private AlertType alertType;
    private AlertStatus alertStatus;
    private String deviceId;

    @Builder
    public AlertSettingUpdateServiceRequest(AlertType alertType, AlertStatus alertStatus, String deviceId) {
        this.alertType = alertType;
        this.alertStatus = alertStatus;
        this.deviceId = deviceId;
    }
}
