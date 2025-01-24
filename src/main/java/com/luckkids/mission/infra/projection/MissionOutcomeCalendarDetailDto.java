package com.luckkids.mission.infra.projection;

import com.luckkids.mission.domain.misson.MissionType;

public record MissionOutcomeCalendarDetailDto(
	MissionType missionType,
	String missionDescription
) {
}
