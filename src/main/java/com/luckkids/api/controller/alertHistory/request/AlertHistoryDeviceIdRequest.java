package com.luckkids.api.controller.alertHistory.request;

import com.luckkids.api.service.alertHistory.request.AlertHistoryDeviceIdServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlertHistoryDeviceIdRequest {

    @NotBlank(message = "디바이스 ID는 필수입니다.")
    private String deviceId;

    @Builder
    private AlertHistoryDeviceIdRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public AlertHistoryDeviceIdServiceRequest toServiceRequest() {
        return AlertHistoryDeviceIdServiceRequest.builder()
            .deviceId(deviceId)
            .build();
    }
}
