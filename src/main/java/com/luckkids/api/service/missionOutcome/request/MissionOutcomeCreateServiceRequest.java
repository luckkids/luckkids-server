package com.luckkids.api.service.missionOutcome.request;

import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.domain.missionOutcome.SuccessChecked.*;

import java.time.LocalDate;

import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.misson.Mission;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeCreateServiceRequest {

	private Mission mission;
	private LocalDate missionDate;

	@Builder
	private MissionOutcomeCreateServiceRequest(Mission mission, LocalDate missionDate) {
		this.mission = mission;
		this.missionDate = missionDate;
	}

	public MissionOutcome toEntity() {
		return MissionOutcome.builder()
			.mission(mission)
			.missionStatus(FAILED)
			.missionDate(missionDate)
			.successChecked(UNCHECKED)
			.build();
	}

}
