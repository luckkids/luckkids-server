package com.luckkids.api.controller.missionOutcome;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.missionOutcome.MissionStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.time.LocalDate.now;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@RestController
public class MissionOutcomeController {

    private final MissionOutcomeService missionOutcomeService;
    private final MissionOutcomeReadService missionOutcomeReadService;
    private final SecurityService securityService;

    @PatchMapping("/api/v1/missionOutComes/{missionOutcomeId}")
    public ApiResponse<Long> updateMissionOutcome(@PathVariable Long missionOutcomeId,
                                                  @Valid @RequestBody MissionOutcomeUpdateRequest request) {
        return ApiResponse.ok(missionOutcomeService.updateMissionOutcome(missionOutcomeId, request.getMissionStatus()));
    }

    @GetMapping("/api/v1/missionOutComes")
    public ApiResponse<List<MissionOutcomeResponse>> getMissionDetailListForStatus(@RequestParam(required = false) MissionStatus missionStatus) {
        int userId = securityService.getCurrentUserInfo().getUserId();
        return ApiResponse.ok(missionOutcomeReadService.getMissionDetailListForStatus(ofNullable(missionStatus), userId, now()));
    }
}
