package com.luckkids.api.service.alertSetting.response;

import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.mission.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingUpdateResponse {
    private AlertStatus entire;
    private AlertStatus mission;
    private AlertStatus luck;
    private AlertStatus friend;
    private AlertStatus notice;

    @Builder
    private AlertSettingUpdateResponse(AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus friend, AlertStatus notice) {
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.friend = friend;
        this.notice = notice;
    }

    public static AlertSettingUpdateResponse of(AlertSetting alertSetting){
        return AlertSettingUpdateResponse.builder()
            .entire(alertSetting.getEntire())
            .mission(alertSetting.getMission())
            .luck(alertSetting.getLuckMessage())
            .notice(alertSetting.getNotice())
            .friend(alertSetting.getFriend())
            .build();
    }
}
