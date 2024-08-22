package com.luckkids.api.service.initialSetting.response;

import com.luckkids.api.service.initialSetting.request.InitialSettingAlertServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class InitialSettingAlertResponse {
    private AlertStatus entire;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus friend;
    private AlertStatus notice;
    private LocalTime luckMessageAlertTime;

    @Builder
    private InitialSettingAlertResponse(AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus friend,AlertStatus notice, LocalTime luckMessageAlertTime) {
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.friend = friend;
        this.notice = notice;
        this.luckMessageAlertTime = luckMessageAlertTime;
    }
}
