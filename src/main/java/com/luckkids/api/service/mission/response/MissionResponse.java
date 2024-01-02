package com.luckkids.api.service.mission.response;

import com.luckkids.api.service.initialSetting.response.InitialSettingMissionResponse;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class MissionResponse {

    private int id;
    private String missionDescription;
    private AlertStatus alertStatus;
    private LocalTime alertTime;

    @Builder
    private MissionResponse(int id, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.id = id;
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }

    public static MissionResponse of(Mission mission) {
        return MissionResponse.builder()
            .id(mission.getId())
            .missionDescription(mission.getMissionDescription())
            .alertStatus(mission.getAlertStatus())
            .alertTime(mission.getAlertTime())
            .build();
    }

    public InitialSettingMissionResponse toInitialSettingResponse(){
        return InitialSettingMissionResponse.builder()
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }
}
