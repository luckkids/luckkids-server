package com.luckkids.api.service.luckMission.response;

import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.misson.MissionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckMissionResponse {

    private MissionType missionType;
    private String description;
    private LocalTime alertTime;

    @Builder
    private LuckMissionResponse(MissionType missionType, String description, LocalTime alertTime) {
        this.missionType = missionType;
        this.description = description;
        this.alertTime = alertTime;
    }

    public static LuckMissionResponse of(LuckkidsMission luckMission) {
        return LuckMissionResponse.builder()
            .missionType(luckMission.getMissionType())
            .description(luckMission.getDescription())
            .alertTime(luckMission.getAlertTime())
            .build();
    }
}
