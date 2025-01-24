package com.luckkids.api.service.alertSetting.response;

import com.luckkids.api.service.initialSetting.response.InitialSettingAlertResponse;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.mission.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AlertSettingResponse {

    private AlertStatus entire;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus friend;
    private AlertStatus notice;
    private LocalTime luckMessageAlertTime;

    @Builder
    private AlertSettingResponse(AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus friend, AlertStatus notice, LocalTime luckMessageAlertTime) {
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.friend = friend;
        this.notice = notice;
        this.luckMessageAlertTime = luckMessageAlertTime;
    }

    public static AlertSettingResponse of(AlertSetting alertSetting){
        return AlertSettingResponse.builder()
            .entire(alertSetting.getEntire())
            .mission(alertSetting.getMission())
            .luck(alertSetting.getLuckMessage())
            .friend(alertSetting.getFriend())
            .notice(alertSetting.getNotice())
            .luckMessageAlertTime(alertSetting.getLuckMessageAlertTime())
            .build();
    }

    public InitialSettingAlertResponse toInitialSettingResponse(){
        return InitialSettingAlertResponse.builder()
            .entire(entire)
            .mission(mission)
            .luck(luck)

            .notice(notice)
            .luckMessageAlertTime(luckMessageAlertTime)
            .build();
    }
}
