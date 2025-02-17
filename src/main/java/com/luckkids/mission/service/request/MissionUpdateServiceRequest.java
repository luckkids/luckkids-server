package com.luckkids.mission.service.request;

import java.time.LocalTime;

import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.mission.domain.misson.MissionActive;
import com.luckkids.mission.domain.misson.MissionType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionUpdateServiceRequest {

	private MissionType missionType;
	private String missionDescription;
	private MissionActive missionActive;
	private AlertStatus alertStatus;
	private LocalTime alertTime;

	@Builder
	private MissionUpdateServiceRequest(MissionType missionType, String missionDescription, MissionActive missionActive,
		AlertStatus alertStatus, LocalTime alertTime) {
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.missionActive = missionActive;
		this.alertStatus = alertStatus;
		this.alertTime = alertTime;
	}
}
