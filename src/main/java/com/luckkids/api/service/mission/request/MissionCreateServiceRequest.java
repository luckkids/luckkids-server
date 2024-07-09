package com.luckkids.api.service.mission.request;

import static com.luckkids.domain.misson.MissionActive.*;

import java.time.LocalDate;
import java.time.LocalTime;

import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionCreateServiceRequest {

	private Integer luckkidsMissionId;
	private MissionType missionType;
	private String missionDescription;
	private AlertStatus alertStatus;
	private LocalTime alertTime;

	@Builder
	private MissionCreateServiceRequest(Integer luckkidsMissionId, MissionType missionType, String missionDescription,
		AlertStatus alertStatus, LocalTime alertTime) {
		this.luckkidsMissionId = luckkidsMissionId;
		this.missionDescription = missionDescription;
		this.missionType = missionType;
		this.alertStatus = alertStatus;
		this.alertTime = alertTime;
	}

	public Mission toEntity(User user) {
		return Mission.builder()
			.user(user)
			.luckkidsMissionId(luckkidsMissionId)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.missionActive(TRUE)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.push_date(LocalDate.of(2024, 1, 1))
			.build();
	}
}
