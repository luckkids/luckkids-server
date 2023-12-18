package com.luckkids.api.controller.home;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeForCalendarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

import static java.time.LocalDate.now;

@RequiredArgsConstructor
@RestController
public class HomeController {

    private final MissionOutcomeReadService missionOutcomeReadService;

    @GetMapping("/api/v1/home/calender")
    public ApiResponse<MissionOutcomeForCalendarResponse> getMissionOutcomeForCalendar(
        @RequestParam(required = false) Optional<LocalDate> missionDate
    ) {
        return ApiResponse.ok(missionOutcomeReadService.getMissionOutcomeForCalendar(missionDate.orElse(now())));
    }
}
