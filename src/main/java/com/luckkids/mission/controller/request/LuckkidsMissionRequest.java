package com.luckkids.mission.controller.request;

import java.time.LocalTime;

import com.luckkids.mission.service.request.LuckkidsMissionServiceRequest;
import com.luckkids.mission.domain.misson.MissionType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LuckkidsMissionRequest {

	@NotNull(message = "미션타입은 필수입니다.")
	private MissionType missionType;
	@NotNull(message = "미션설명은 필수입니다.")
	private String missionDescription;
	@NotNull(message = "알림시간은 필수입니다.")
	private LocalTime alertTime;
	@Min(value = 0, message = "정렬값은 0 이상이어야 합니다.")
	private int sort;

	@Builder
	private LuckkidsMissionRequest(MissionType missionType, String missionDescription, LocalTime alertTime, int sort) {
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.alertTime = alertTime;
		this.sort = sort;
	}

	public LuckkidsMissionServiceRequest toServiceRequest() {
		return LuckkidsMissionServiceRequest.builder()
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertTime(alertTime)
			.sort(sort)
			.build();
	}
}
