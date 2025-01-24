package com.luckkids.mission.service.request;

import com.luckkids.mission.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.mission.domain.misson.MissionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckkidsMissionServiceRequest {

    private MissionType missionType;
    private String missionDescription;
    private LocalTime alertTime;
    private int sort;

    @Builder
    private LuckkidsMissionServiceRequest(MissionType missionType, String missionDescription, LocalTime alertTime, int sort) {
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
        this.sort = sort;
    }

    public LuckkidsMission toEntity() {
        return LuckkidsMission.builder()
                .missionType(missionType)
                .missionDescription(missionDescription)
                .alertTime(alertTime)
                .sort(sort)
                .build();
    }

}
