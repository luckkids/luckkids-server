package com.luckkids.api.controller.alertSetting.request;

import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.misson.AlertStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingUpdateRequest {

    @NotNull(message = "알림타입은 필수입니다.")
    private AlertType alertType;
    @NotNull(message = "알림상태는 필수입니다.")
    private AlertStatus alertStatus;
    @NotBlank(message = "디바이스ID는 필수입니다.")
    private String deviceId;

    @Builder
    private AlertSettingUpdateRequest(AlertType alertType, AlertStatus alertStatus, String deviceId) {
        this.alertType = alertType;
        this.alertStatus = alertStatus;
        this.deviceId = deviceId;
    }

    public AlertSettingUpdateServiceRequest toServiceRequest(){
        return AlertSettingUpdateServiceRequest.builder()
            .alertType(alertType)
            .alertStatus(alertStatus)
            .deviceId(deviceId)
            .build();
    }
}
