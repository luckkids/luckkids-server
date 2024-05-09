package com.luckkids.domain.missionOutcome.projection;

import java.time.LocalTime;

import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.MissionType;

public record MissionOutcomeDetailDto(
	Long id,
	MissionType missionType,
	String missionDescription,
	AlertStatus alertStatus,
	LocalTime alertTime,
	MissionStatus missionStatus) {

	public MissionOutcomeResponse toMissionOutcomeResponse() {
		return MissionOutcomeResponse.builder()
			.id(id)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.missionStatus(missionStatus)
			.build();
	}
}
