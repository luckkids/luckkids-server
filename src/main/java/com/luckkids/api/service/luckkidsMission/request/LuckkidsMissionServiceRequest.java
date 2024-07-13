package com.luckkids.api.service.luckkidsMission.request;

import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.api.service.push.request.SendPushDataDto;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.push.PushMessage;
import com.luckkids.domain.push.PushScreenName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckkidsMissionServiceRequest {

    private MissionType missionType;
    private String missionDescription;
    private LocalTime alertTime;

    @Builder
    private LuckkidsMissionServiceRequest(MissionType missionType, String missionDescription, LocalTime alertTime) {
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
    }

    public SendPushAlertTypeServiceRequest toSendPushAlertTypeRequest(AlertType alertType){
        return SendPushAlertTypeServiceRequest.builder()
            .sendPushDataDto(
                    SendPushDataDto.builder()
                            .alert_destination_type(PushScreenName.MISSION.getText())
                            .build()
            )
            .body(PushMessage.MISSION.getText())
            .alertType(alertType)
            .build();
    }

    public LuckkidsMission toEntity(){
        return LuckkidsMission.builder()
            .missionType(missionType)
            .missionDescription(missionDescription)
            .alertTime(alertTime)
            .build();
    }

}
