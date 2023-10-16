package com.luckkids.api.service.mission.request;

import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MissionCreateServiceRequest {

    private String missionDescription;
    private AlertStatus alertStatus;
    private LocalTime alertTime;

    @Builder
    public MissionCreateServiceRequest(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }

    public Mission toEntity(User user) {
        return Mission.builder()
            .user(user)
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .build();
    }
}
