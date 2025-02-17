package com.luckkids.mission.controller.request;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.luckkids.mission.service.request.MissionUpdateServiceRequest;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.mission.domain.misson.MissionActive;
import com.luckkids.mission.domain.misson.MissionType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionUpdateRequest {

	private MissionType missionType;
	private String missionDescription;
	private MissionActive missionActive;
	private AlertStatus alertStatus;

	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime alertTime;

	@Builder
	private MissionUpdateRequest(MissionType missionType, String missionDescription, MissionActive missionActive,
		AlertStatus alertStatus, LocalTime alertTime) {
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.missionActive = missionActive;
		this.alertStatus = alertStatus;
		this.alertTime = alertTime;
	}

	public MissionUpdateServiceRequest toServiceRequest() {
		return MissionUpdateServiceRequest.builder()
			.missionType(missionType)
			.missionDescription(missionDescription)
			.missionActive(missionActive)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.build();
	}
}