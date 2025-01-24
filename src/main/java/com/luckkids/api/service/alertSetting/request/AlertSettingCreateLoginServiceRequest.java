package com.luckkids.api.service.alertSetting.request;

import com.luckkids.mission.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingCreateLoginServiceRequest {

    private int userId;
    private String deviceId;
    private AlertStatus alertStatus;

    @Builder
    private AlertSettingCreateLoginServiceRequest(int userId, String deviceId, AlertStatus alertStatus) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.alertStatus = alertStatus;
    }


}
