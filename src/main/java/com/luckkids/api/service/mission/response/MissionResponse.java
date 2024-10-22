package com.luckkids.api.service.mission.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.luckkids.api.service.initialSetting.response.InitialSettingMissionResponse;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionActive;
import com.luckkids.domain.misson.MissionType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionResponse {

	private int id;
	private Integer luckkidsMissionId;
	private MissionType missionType;
	private String missionDescription;
	private MissionActive missionActive;
	private AlertStatus alertStatus;
	private LocalTime alertTime;
	private LocalDateTime createdDate;

	@Builder
	private MissionResponse(int id, Integer luckkidsMissionId, MissionType missionType, String missionDescription,
		MissionActive missionActive, AlertStatus alertStatus, LocalTime alertTime, LocalDateTime createdDate) {
		this.id = id;
		this.luckkidsMissionId = luckkidsMissionId;
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.missionActive = missionActive;
		this.alertStatus = alertStatus;
		this.alertTime = alertTime;
		this.createdDate = createdDate;
	}

	public static MissionResponse of(Mission mission) {
		return MissionResponse.builder()
			.id(mission.getId())
			.luckkidsMissionId(mission.getLuckkidsMissionId())
			.missionType(mission.getMissionType())
			.missionDescription(mission.getMissionDescription())
			.missionActive(mission.getMissionActive())
			.alertStatus(mission.getAlertStatus())
			.alertTime(mission.getAlertTime())
			.createdDate(mission.getCreatedDate())
			.build();
	}

	public InitialSettingMissionResponse toInitialSettingResponse() {
		return InitialSettingMissionResponse.builder()
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.build();
	}
}
