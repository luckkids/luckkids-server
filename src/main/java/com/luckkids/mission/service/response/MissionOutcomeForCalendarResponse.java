package com.luckkids.mission.service.response;

import java.time.LocalDate;
import java.util.List;

import com.luckkids.mission.infra.projection.MissionOutcomeCalendarDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeForCalendarResponse {

	private LocalDate startDate;
	private LocalDate endDate;
	private List<MissionOutcomeCalendarDto> calendar;

	@Builder
	private MissionOutcomeForCalendarResponse(LocalDate startDate, LocalDate endDate,
		List<MissionOutcomeCalendarDto> calendar) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.calendar = calendar;
	}

	public static MissionOutcomeForCalendarResponse of(LocalDate startDate, LocalDate endDate,
		List<MissionOutcomeCalendarDto> calendar) {
		return MissionOutcomeForCalendarResponse.builder()
			.startDate(startDate)
			.endDate(endDate)
			.calendar(calendar)
			.build();
	}
}
