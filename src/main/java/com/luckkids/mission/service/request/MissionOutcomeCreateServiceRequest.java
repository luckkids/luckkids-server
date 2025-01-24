package com.luckkids.mission.service.request;

import static com.luckkids.mission.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.mission.domain.missionOutcome.SuccessChecked.*;

import java.time.LocalDate;

import com.luckkids.mission.domain.missionOutcome.MissionOutcome;
import com.luckkids.mission.domain.misson.Mission;

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
