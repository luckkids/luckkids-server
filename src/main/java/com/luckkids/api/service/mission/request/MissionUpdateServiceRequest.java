package com.luckkids.api.service.mission.request;

import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MissionUpdateServiceRequest {

    private String missionDescription;
    private AlertStatus alertStatus;
    private LocalTime alertTime;

    @Builder
    private MissionUpdateServiceRequest(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }
}
