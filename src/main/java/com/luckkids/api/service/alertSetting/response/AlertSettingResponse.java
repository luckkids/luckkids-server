package com.luckkids.api.service.alertSetting.response;

import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingResponse {
    private String sound;
    private AlertStatus all;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus notice;

    @Builder
    private AlertSettingResponse(String sound, AlertStatus all, AlertStatus mission, AlertStatus luck, AlertStatus notice) {
        this.sound = sound;
        this.all = all;
        this.mission = mission;
        this.luck = luck;
        this.notice = notice;
    }

    public static AlertSettingResponse of(AlertSetting alertSetting){
        return AlertSettingResponse.builder()
            .sound(alertSetting.getSound())
//            .all(alertSetting.getAll())
//            .mission(alertSetting.getMission())
//            .luck(alertSetting.getLuck())
//            .notice(alertSetting.getNotice())
            .build();
    }
}
