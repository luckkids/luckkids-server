package com.luckkids.api.controller.missionOutcome;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MissionOutcomeController {

    private final MissionOutcomeService missionOutcomeService;

    @PatchMapping("/api/v1/missionOutComes/{missionOutcomeId}")
    public ApiResponse<Long> updateMissionOutcome(@PathVariable Long missionOutcomeId,
                                                  @Valid @RequestBody MissionOutcomeUpdateRequest request) {
        return ApiResponse.ok(missionOutcomeService.updateMissionOutcome(missionOutcomeId, request.getMissionStatus()));
    }
}
