package com.luckkids.api.service.missionOutcome.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeCountResponse {
	private Long count;

	@Builder
	private MissionOutcomeCountResponse(Long count) {
		this.count = count;
	}

	public static MissionOutcomeCountResponse of(Long count) {
		return MissionOutcomeCountResponse.builder()
			.count(count)
			.build();
	}
}
