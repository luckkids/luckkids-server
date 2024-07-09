package com.luckkids.api.service.mission.response;

import java.util.List;
import java.util.Map;

import com.luckkids.domain.misson.MissionType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionAggregateResponse {
	private Map<MissionType, List<MissionResponse>> userMissions;
	private Map<MissionType, List<RemainingLuckkidsMissionResponse>> luckkidsMissions;

	@Builder
	private MissionAggregateResponse(Map<MissionType, List<MissionResponse>> userMissions,
		Map<MissionType, List<RemainingLuckkidsMissionResponse>> luckkidsMissions) {
		this.userMissions = userMissions;
		this.luckkidsMissions = luckkidsMissions;
	}

	public static MissionAggregateResponse of(Map<MissionType, List<MissionResponse>> userMissions,
		Map<MissionType, List<RemainingLuckkidsMissionResponse>> luckkidsMissions) {
		return MissionAggregateResponse.builder()
			.userMissions(userMissions)
			.luckkidsMissions(luckkidsMissions)
			.build();
	}
}
