package com.luckkids.api.controller.mission.request;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.MissionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionCreateRequest {

	private Integer luckkidsMissionId;

	@NotNull(message = "미션 타입은 필수입니다.")
	private MissionType missionType;

	@NotBlank(message = "미션 내용은 필수입니다.")
	private String missionDescription;

	@NotNull(message = "알람 여부는 필수입니다.")
	private AlertStatus alertStatus;

	@NotNull(message = "알람 시간은 필수입니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime alertTime;

	@Builder
	private MissionCreateRequest(Integer luckkidsMissionId, MissionType missionType, String missionDescription,
		AlertStatus alertStatus, LocalTime alertTime) {
		this.luckkidsMissionId = luckkidsMissionId;
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.alertStatus = alertStatus;
		this.alertTime = alertTime;
	}

	public MissionCreateServiceRequest toServiceRequest() {
		return MissionCreateServiceRequest.builder()
			.luckkidsMissionId(luckkidsMissionId)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.build();
	}
}