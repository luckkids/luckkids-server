package com.luckkids.api.controller.missionOutcome;

import static java.time.LocalDate.*;
import static java.util.Optional.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.missionOutcome.request.MissionOutcomeUpdateRequest;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeCountResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeUpdateResponse;
import com.luckkids.domain.missionOutcome.MissionStatus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MissionOutcomeController {

	private final MissionOutcomeService missionOutcomeService;
	private final MissionOutcomeReadService missionOutcomeReadService;

	@PatchMapping("/api/v1/missionOutcomes/{missionOutcomeId}")
	public ApiResponse<MissionOutcomeUpdateResponse> updateMissionOutcome(@PathVariable Long missionOutcomeId,
		@Valid @RequestBody MissionOutcomeUpdateRequest request) {
		return ApiResponse.ok(missionOutcomeService.updateMissionOutcome(missionOutcomeId, request.getMissionStatus()));
	}

	@GetMapping("/api/v1/missionOutcomes")
	public ApiResponse<List<MissionOutcomeResponse>> getMissionDetailListForStatus(
		@RequestParam(required = false) MissionStatus missionStatus) {
		return ApiResponse.ok(
			missionOutcomeReadService.getMissionOutcomeDetailListForStatus(ofNullable(missionStatus), now()));
	}

	@GetMapping("/api/v1/missionOutcomes/count")
	public ApiResponse<MissionOutcomeCountResponse> getMissionOutcomesCount() {
		return ApiResponse.ok(missionOutcomeReadService.getMissionOutcomesCount());
	}
}
