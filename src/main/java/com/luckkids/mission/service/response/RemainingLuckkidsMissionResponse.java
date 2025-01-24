package com.luckkids.mission.service.response;

import java.time.LocalTime;

import com.luckkids.mission.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.mission.domain.misson.MissionType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RemainingLuckkidsMissionResponse {

	private int luckkidsMissionId;
	private MissionType missionType;
	private String missionDescription;
	private LocalTime alertTime;

	@Builder
	private RemainingLuckkidsMissionResponse(int luckkidsMissionId, MissionType missionType, String missionDescription,
		LocalTime alertTime) {
		this.luckkidsMissionId = luckkidsMissionId;
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.alertTime = alertTime;
	}

	public static RemainingLuckkidsMissionResponse of(LuckkidsMission luckMission) {
		return RemainingLuckkidsMissionResponse.builder()
			.luckkidsMissionId(luckMission.getId())
			.missionType(luckMission.getMissionType())
			.missionDescription(luckMission.getMissionDescription())
			.alertTime(luckMission.getAlertTime())
			.build();
	}
}
