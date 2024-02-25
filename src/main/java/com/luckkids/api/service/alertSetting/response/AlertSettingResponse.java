package com.luckkids.api.service.alertSetting.response;

import com.luckkids.api.service.initialSetting.response.InitialSettingAlertResponse;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingResponse {

    private AlertStatus entire;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus notice;

    @Builder
    private AlertSettingResponse(AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus notice) {
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.notice = notice;
    }

    public static AlertSettingResponse of(AlertSetting alertSetting){
        return AlertSettingResponse.builder()
            .entire(alertSetting.getEntire())
            .mission(alertSetting.getMission())
            .luck(alertSetting.getLuckMessage())
            .notice(alertSetting.getNotice())
            .build();
    }

    public InitialSettingAlertResponse toInitialSettingResponse(){
        return InitialSettingAlertResponse.builder()
            .entire(entire)
            .mission(mission)
            .luck(luck)
            .notice(notice)
            .build();
    }
}
