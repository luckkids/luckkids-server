package com.luckkids.api.service.mission.request;

import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.MissionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MissionUpdateServiceRequest {

    private MissionType missionType;
    private String missionDescription;
    private AlertStatus alertStatus;
    private LocalTime alertTime;

    @Builder
    private MissionUpdateServiceRequest(MissionType missionType, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }
}
