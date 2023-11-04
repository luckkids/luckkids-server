package com.luckkids.domain.missionOutcome.projection;

import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.domain.missionOutcome.MissionStatus;

import java.time.LocalTime;

public record MissionOutcomeDetailDto(
    Long id,
    String missionDescription,
    LocalTime alertTime,
    MissionStatus missionStatus) {

    public MissionOutcomeResponse toMissionOutcomeResponse() {
        return MissionOutcomeResponse.builder()
            .id(id)
            .missionDescription(missionDescription)
            .alertTime(alertTime)
            .missionStatus(missionStatus)
            .build();
    }
}
