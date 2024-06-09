package com.luckkids.domain.missionOutcome.projection;

import java.time.LocalDate;

public record MissionOutcomeCalendarDto(
	LocalDate missionDate,
	boolean hasSucceed
) {
}
