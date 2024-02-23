package com.luckkids.domain.missionOutcome.projection;

import com.luckkids.domain.misson.MissionType;

public record MissionOutcomeCalenderDetailDto(
    MissionType missionType,
    String missionDescription
) {
}
