package com.luckkids.api.controller.home;

import static java.time.LocalDate.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.home.response.HomeMainResponse;
import com.luckkids.api.service.alertHistory.AlertHistoryReadService;
import com.luckkids.mission.service.MissionOutcomeReadService;
import com.luckkids.mission.service.response.MissionOutcomeForCalendarResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.UserCharacterService;
import com.luckkids.mission.infra.projection.MissionOutcomeCalendarDetailDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class HomeController {

	private final MissionOutcomeReadService missionOutcomeReadService;
	private final UserReadService userReadService;
	private final UserCharacterService userCharacterService;
	private final AlertHistoryReadService alertHistoryReadService;

	@GetMapping("/api/v1/home/calendar")
	public ApiResponse<MissionOutcomeForCalendarResponse> getMissionOutcomeForCalendar(
		@RequestParam(required = false) Optional<LocalDate> missionDate
	) {
		return ApiResponse.ok(
			missionOutcomeReadService.getMissionOutcomeForCalendar(
				missionDate.orElse(now()),
				date -> date.withDayOfMonth(1).minusMonths(1),
				date -> date.withDayOfMonth(1).plusMonths(1).minusDays(1)
			)
		);
	}

	@GetMapping("/api/v1/home/calendar/{missionDate}")
	public ApiResponse<List<MissionOutcomeCalendarDetailDto>> getMissionOutcomeForCalendarDetail(
		@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate missionDate
	) {
		return ApiResponse.ok(missionOutcomeReadService.getMissionOutcomeForCalendarDetail(missionDate));
	}

	@GetMapping("/api/v1/home/main")
	public ApiResponse<HomeMainResponse> getHomeMainInfo() {
		return ApiResponse.ok(
			HomeMainResponse.of(
				userReadService.getCharacterAchievementRate(),
				userCharacterService.getCharacterSummary(),
				missionOutcomeReadService.getMissionOutcomeForCalendar(
					now(),
					date -> date.minusDays(3),
					date -> date.plusDays(3)
				),
				alertHistoryReadService.hasUncheckedAlerts()
			)
		);
	}
}
