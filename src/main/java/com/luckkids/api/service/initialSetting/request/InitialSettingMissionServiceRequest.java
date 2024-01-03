package com.luckkids.api.service.initialSetting.request;

import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class InitialSettingMissionServiceRequest {
    private String missionDescription;
    private LocalTime alertTime;

    @Builder
    private InitialSettingMissionServiceRequest(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
    }

    public MissionCreateServiceRequest toMissionServiceRequest(AlertStatus alertStatus){
        return MissionCreateServiceRequest.builder()
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }
}
