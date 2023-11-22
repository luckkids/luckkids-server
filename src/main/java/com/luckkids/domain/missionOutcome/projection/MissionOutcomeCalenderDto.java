package com.luckkids.domain.missionOutcome.projection;

import java.time.LocalDate;

public record MissionOutcomeCalenderDto(
    LocalDate missionDate,
    boolean hasSucceed
) {
}
