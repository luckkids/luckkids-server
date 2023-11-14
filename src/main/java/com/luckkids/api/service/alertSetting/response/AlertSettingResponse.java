package com.luckkids.api.service.alertSetting.response;

import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingResponse {

    private int id;
    private AlertStatus entire;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus notice;

    @Builder
    private AlertSettingResponse(int id, AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus notice) {
        this.id = id;
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.notice = notice;
    }

    public static AlertSettingResponse of(AlertSetting alertSetting){
        return AlertSettingResponse.builder()
            .id(alertSetting.getId())
            .entire(alertSetting.getEntire())
            .mission(alertSetting.getMission())
            .luck(alertSetting.getLuck())
            .notice(alertSetting.getNotice())
            .build();
    }
}
