package com.luckkids.api.service.mission.request;

import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MissionCreateServiceRequest {

    private MissionType missionType;
    private String missionDescription;
    private AlertStatus alertStatus;
    private LocalTime alertTime;

    @Builder
    private MissionCreateServiceRequest(MissionType missionType, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.missionType = missionType;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }

    public Mission toEntity(User user) {
        return Mission.builder()
            .user(user)
            .missionType(missionType)
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .push_date(LocalDate.of(2024, 1, 1))
            .build();
    }
}
