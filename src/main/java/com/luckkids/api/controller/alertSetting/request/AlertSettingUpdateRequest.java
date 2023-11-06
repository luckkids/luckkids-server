package com.luckkids.api.controller.alertSetting.request;

import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingUpdateRequest {
    private AlertType alertType;
    private AlertStatus alertStatus;

    public AlertSettingUpdateServiceRequest toServiceRequest(){
        return AlertSettingUpdateServiceRequest.builder()
            .alertType(alertType)
            .alertStatus(alertStatus)
            .build();
    }
}
