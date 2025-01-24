package com.luckkids.mission.infra.projection;

import java.time.LocalDate;

public record MissionOutcomeCalendarDto(
	LocalDate missionDate,
	boolean hasSucceed
) {
}
