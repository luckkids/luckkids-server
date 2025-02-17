package com.luckkids.mission.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionDeleteResponse {
	private int missionId;

	@Builder
	private MissionDeleteResponse(int missionId) {
		this.missionId = missionId;
	}

	public static MissionDeleteResponse of(int missionId) {
		return MissionDeleteResponse.builder()
			.missionId(missionId)
			.build();
	}
}
