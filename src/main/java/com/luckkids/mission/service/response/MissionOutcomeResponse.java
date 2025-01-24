package com.luckkids.mission.service.response;

import java.time.LocalTime;

import com.luckkids.mission.domain.missionOutcome.MissionStatus;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.mission.domain.misson.MissionType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeResponse {

	private Long id;
	private MissionType missionType;
	private String missionDescription;
	private AlertStatus alertStatus;
	private LocalTime alertTime;
	private MissionStatus missionStatus;

	@Builder
	private MissionOutcomeResponse(Long id, MissionType missionType, String missionDescription, AlertStatus alertStatus,
		LocalTime alertTime, MissionStatus missionStatus) {
		this.id = id;
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.alertStatus = alertStatus;
		this.alertTime = alertTime;
		this.missionStatus = missionStatus;
	}
}
