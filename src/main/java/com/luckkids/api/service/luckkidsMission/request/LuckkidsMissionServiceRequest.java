package com.luckkids.api.service.luckkidsMission.request;

import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.push.PushMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckkidsMissionServiceRequest {

    private MissionType missionType;
    private String description;
    private LocalTime alertTime;

    @Builder
    private LuckkidsMissionServiceRequest(MissionType missionType, String description, LocalTime alertTime) {
        this.missionType = missionType;
        this.description = description;
        this.alertTime = alertTime;
    }

    public SendPushAlertTypeServiceRequest toSendPushAlertTypeRequest(AlertType alertType){
        return SendPushAlertTypeServiceRequest.builder()
            .screenName("MISSION")
            .body(PushMessage.MISSION.getText())
            .alertType(alertType)
            .build();
    }

    public LuckkidsMission toEntity(){
        return LuckkidsMission.builder()
            .missionType(missionType)
            .description(description)
            .alertTime(alertTime)
            .build();
    }

}
