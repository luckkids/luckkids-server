package com.luckkids.api.service.initialSetting.request;

import com.luckkids.api.service.alertSetting.request.AlertSettingCreateServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingAlertServiceRequest {

    private String deviceId;
    private AlertStatus alertStatus;

    @Builder
    private InitialSettingAlertServiceRequest(String deviceId, AlertStatus alertStatus) {
        this.deviceId = deviceId;
        this.alertStatus = alertStatus;
    }

    public AlertSettingCreateServiceRequest toServiceRequest(){
        return AlertSettingCreateServiceRequest.builder()
            .deviceId(deviceId)
            .alertStatus(alertStatus)
            .build();
    }
}
