package com.luckkids.api.service.alertSetting.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AlertSettingLuckMessageAlertTimeServiceRequest {

    private LocalTime luckMessageAlertTime;
    private String deviceId;

    @Builder
    private AlertSettingLuckMessageAlertTimeServiceRequest(LocalTime luckMessageAlertTime, String deviceId) {
        this.luckMessageAlertTime = luckMessageAlertTime;
        this.deviceId = deviceId;
    }
}
