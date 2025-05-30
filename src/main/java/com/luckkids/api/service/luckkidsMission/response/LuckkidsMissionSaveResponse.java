package com.luckkids.api.service.luckkidsMission.response;

import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.misson.MissionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckkidsMissionSaveResponse {

    private int id;
    private MissionType missionType;
    private String missionDescription;
    private LocalTime alertTime;
    private int sort;

    @Builder
    private LuckkidsMissionSaveResponse(int id, MissionType missionType, String missionDescription, LocalTime alertTime, int sort) {
        this.id = id;
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
        this.sort = sort;
    }

    public static LuckkidsMissionSaveResponse of(LuckkidsMission luckkidsMission) {
        return LuckkidsMissionSaveResponse.builder()
                .id(luckkidsMission.getId())
                .missionType(luckkidsMission.getMissionType())
                .missionDescription(luckkidsMission.getMissionDescription())
                .alertTime(luckkidsMission.getAlertTime())
                .sort(luckkidsMission.getSort())
                .build();
    }
}
