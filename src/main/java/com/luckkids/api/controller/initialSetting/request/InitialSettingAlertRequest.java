package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingAlertServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingAlertRequest {
    private AlertStatus alertStatus;

    @Builder
    private InitialSettingAlertRequest(AlertStatus alertStatus) {
        this.alertStatus = alertStatus;
    }

    public InitialSettingAlertServiceRequest toServiceRequest(){
        return InitialSettingAlertServiceRequest.builder()
            .alertStatus(alertStatus)
            .build();
    }
}
