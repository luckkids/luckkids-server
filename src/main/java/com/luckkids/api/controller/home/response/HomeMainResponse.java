package com.luckkids.api.controller.home.response;

import com.luckkids.mission.service.response.MissionOutcomeForCalendarResponse;
import com.luckkids.api.service.userCharacter.response.UserCharacterSummaryResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeMainResponse {
	private double luckkidsAchievementRate;
	private UserCharacterSummaryResponse userCharacterSummaryResponse;
	private MissionOutcomeForCalendarResponse missionOutcomeForWeekResponse;
	private boolean hasUncheckedAlerts;

	@Builder
	private HomeMainResponse(
		double luckkidsAchievementRate,
		UserCharacterSummaryResponse userCharacterSummaryResponse,
		MissionOutcomeForCalendarResponse missionOutcomeForWeekResponse,
		boolean hasUncheckedAlerts
	) {
		this.luckkidsAchievementRate = luckkidsAchievementRate;
		this.userCharacterSummaryResponse = userCharacterSummaryResponse;
		this.missionOutcomeForWeekResponse = missionOutcomeForWeekResponse;
		this.hasUncheckedAlerts = hasUncheckedAlerts;
	}

	public static HomeMainResponse of(
		double luckkidsAchievementRate,
		UserCharacterSummaryResponse userCharacterSummaryResponse,
		MissionOutcomeForCalendarResponse missionOutcomeForWeekResponse,
		boolean hasUncheckedAlerts
	) {
		return HomeMainResponse.builder()
			.luckkidsAchievementRate(luckkidsAchievementRate)
			.userCharacterSummaryResponse(userCharacterSummaryResponse)
			.missionOutcomeForWeekResponse(missionOutcomeForWeekResponse)
			.hasUncheckedAlerts(hasUncheckedAlerts)
			.build();
	}
}
