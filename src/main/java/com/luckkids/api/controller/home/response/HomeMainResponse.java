package com.luckkids.api.controller.home.response;

import com.luckkids.api.service.missionOutcome.response.MissionOutcomeForCalendarResponse;
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

    @Builder
    private HomeMainResponse(
        double luckkidsAchievementRate,
        UserCharacterSummaryResponse userCharacterSummaryResponse,
        MissionOutcomeForCalendarResponse missionOutcomeForWeekResponse
    ) {
        this.luckkidsAchievementRate = luckkidsAchievementRate;
        this.userCharacterSummaryResponse = userCharacterSummaryResponse;
        this.missionOutcomeForWeekResponse = missionOutcomeForWeekResponse;
    }

    public static HomeMainResponse of(
        double luckkidsAchievementRate,
        UserCharacterSummaryResponse userCharacterSummaryResponse,
        MissionOutcomeForCalendarResponse missionOutcomeForWeekResponse
    ) {
        return HomeMainResponse.builder()
            .luckkidsAchievementRate(luckkidsAchievementRate)
            .userCharacterSummaryResponse(userCharacterSummaryResponse)
            .missionOutcomeForWeekResponse(missionOutcomeForWeekResponse)
            .build();
    }
}
