package com.luckkids.api.service.alertHistory.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertHistoryDeviceIdServiceRequest {
    private String deviceId;

    @Builder
    private AlertHistoryDeviceIdServiceRequest(String deviceId) {
        this.deviceId = deviceId;
    }
}

