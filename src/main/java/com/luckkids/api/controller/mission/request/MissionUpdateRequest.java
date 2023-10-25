package com.luckkids.api.controller.mission.request;

import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MissionUpdateRequest {

    private String missionDescription;

    private AlertStatus alertStatus;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime alertTime;

    @Builder
    private MissionUpdateRequest(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }

    public MissionUpdateServiceRequest toServiceRequest() {
        return MissionUpdateServiceRequest.builder()
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }
}