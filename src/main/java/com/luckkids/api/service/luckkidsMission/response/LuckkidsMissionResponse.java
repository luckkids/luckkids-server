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

    private int id;
    private MissionType missionType;
    private String missionDescription;
    private LocalTime alertTime;

    @Builder
    private LuckkidsMissionResponse(int id, MissionType missionType, String missionDescription, LocalTime alertTime) {
        this.id = id;
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
    }

    public static LuckkidsMissionResponse of(LuckkidsMission luckMission) {
        return LuckkidsMissionResponse.builder()
            .id(luckMission.getId())
            .missionType(luckMission.getMissionType())
            .missionDescription(luckMission.getMissionDescription())
            .alertTime(luckMission.getAlertTime())
            .build();
    }
}
