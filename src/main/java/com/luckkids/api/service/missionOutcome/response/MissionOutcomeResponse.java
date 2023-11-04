package com.luckkids.api.service.missionOutcome.response;

import com.luckkids.domain.missionOutcome.MissionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MissionOutcomeResponse {

    private Long id;
    private String missionDescription;
    private LocalTime alertTime;
    private MissionStatus missionStatus;

    @Builder
    private MissionOutcomeResponse(Long id, String missionDescription, LocalTime alertTime, MissionStatus missionStatus) {
        this.id = id;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
        this.missionStatus = missionStatus;
    }
}
