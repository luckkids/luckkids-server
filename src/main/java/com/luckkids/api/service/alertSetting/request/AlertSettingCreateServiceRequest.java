package com.luckkids.api.service.alertSetting.request;

import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingCreateServiceRequest {

    private AlertStatus alertStatus;

    @Builder
    private AlertSettingCreateServiceRequest(AlertStatus alertStatus) {
        this.alertStatus = alertStatus;
    }


}
