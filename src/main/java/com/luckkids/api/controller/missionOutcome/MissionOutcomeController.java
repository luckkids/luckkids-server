package com.luckkids.api.controller.missionOutcome;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
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

    @PatchMapping("/api/v1/missionOutcomes/{missionOutcomeId}")
    public ApiResponse<Integer> updateMissionOutcome(@PathVariable Long missionOutcomeId,
                                                     @Valid @RequestBody MissionOutcomeUpdateRequest request) {
        return ApiResponse.ok(missionOutcomeService.updateMissionOutcome(missionOutcomeId, request.getMissionStatus()));
    }

    @GetMapping("/api/v1/missionOutcomes")
    public ApiResponse<List<MissionOutcomeResponse>> getMissionDetailListForStatus(@RequestParam(required = false) MissionStatus missionStatus) {

        return ApiResponse.ok(missionOutcomeReadService.getMissionDetailListForStatus(ofNullable(missionStatus), now()));
    }

}
