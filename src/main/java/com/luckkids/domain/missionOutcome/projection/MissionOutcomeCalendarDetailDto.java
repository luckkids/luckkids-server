package com.luckkids.domain.missionOutcome.projection;

import com.luckkids.domain.misson.MissionType;

public record MissionOutcomeCalendarDetailDto(
	MissionType missionType,
	String missionDescription
) {
}
