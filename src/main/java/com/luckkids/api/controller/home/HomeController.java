package com.luckkids.api.controller.home;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.home.response.HomeMainResponse;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeForCalendarResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.UserCharacterService;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalenderDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;

@RequiredArgsConstructor
@RestController
public class HomeController {

    private final MissionOutcomeReadService missionOutcomeReadService;
    private final UserReadService userReadService;
    private final UserCharacterService userCharacterService;

    @GetMapping("/api/v1/home/calender")
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

    @GetMapping("/api/v1/home/calender/{missionDate}")
    public ApiResponse<List<MissionOutcomeCalenderDetailDto>> getMissionOutcomeForCalendarDetail(
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
                )
            )
        );
    }
}
