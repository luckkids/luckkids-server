package com.luckkids.mission.infra.projection;

import java.time.LocalTime;

import com.luckkids.mission.service.response.MissionOutcomeResponse;
import com.luckkids.mission.domain.missionOutcome.MissionStatus;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.mission.domain.misson.MissionType;

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
