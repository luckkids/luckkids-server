package com.luckkids.api.service.alertSetting.response;

import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AlertSettingLuckMessageAlertTimeResponse {

    private AlertStatus entire;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus notice;
    private LocalTime luckMessageAlertTime;

    @Builder
    private AlertSettingLuckMessageAlertTimeResponse(AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus notice, LocalTime luckMessageAlertTime) {
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.notice = notice;
        this.luckMessageAlertTime = luckMessageAlertTime;
    }

    public static AlertSettingLuckMessageAlertTimeResponse of(AlertSetting alertSetting){
        return AlertSettingLuckMessageAlertTimeResponse.builder()
                .entire(alertSetting.getEntire())
                .mission(alertSetting.getMission())
                .luck(alertSetting.getLuckMessage())
                .notice(alertSetting.getNotice())
                .luckMessageAlertTime(alertSetting.getLuckMessageAlertTime())
                .build();
    }
}
