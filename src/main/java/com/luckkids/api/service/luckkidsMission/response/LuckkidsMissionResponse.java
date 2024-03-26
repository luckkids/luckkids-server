package com.luckkids.api.service.luckkidsMission.response;

import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.misson.MissionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckkidsMissionResponse {

    private MissionType missionType;
    private String description;
    private LocalTime alertTime;

    @Builder
    private LuckkidsMissionResponse(MissionType missionType, String description, LocalTime alertTime) {
        this.missionType = missionType;
        this.description = description;
        this.alertTime = alertTime;
    }

    public static LuckkidsMissionResponse of(LuckkidsMission luckMission) {
        return LuckkidsMissionResponse.builder()
            .missionType(luckMission.getMissionType())
            .description(luckMission.getDescription())
            .alertTime(luckMission.getAlertTime())
            .build();
    }
}
