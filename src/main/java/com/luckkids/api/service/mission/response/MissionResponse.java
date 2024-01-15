package com.luckkids.api.service.mission.response;

import com.luckkids.api.service.initialSetting.response.InitialSettingMissionResponse;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class MissionResponse {

    private int id;
    private MissionType missionType;
    private String missionDescription;
    private AlertStatus alertStatus;
    private LocalTime alertTime;

    @Builder
    private MissionResponse(int id, MissionType missionType, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.id = id;
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }

    public static MissionResponse of(Mission mission) {
        return MissionResponse.builder()
            .id(mission.getId())
            .missionType(mission.getMissionType())
            .missionDescription(mission.getMissionDescription())
            .alertStatus(mission.getAlertStatus())
            .alertTime(mission.getAlertTime())
            .build();
    }

    public InitialSettingMissionResponse toInitialSettingResponse() {
        return InitialSettingMissionResponse.builder()
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }
}
