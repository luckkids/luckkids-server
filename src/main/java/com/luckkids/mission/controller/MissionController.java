package com.luckkids.mission.controller;

import static java.time.LocalDateTime.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.mission.controller.request.MissionCreateRequest;
import com.luckkids.mission.controller.request.MissionUpdateRequest;
import com.luckkids.mission.service.MissionReadService;
import com.luckkids.mission.service.MissionService;
import com.luckkids.mission.service.response.MissionAggregateResponse;
import com.luckkids.mission.service.MissionDeleteResponse;
import com.luckkids.mission.service.MissionResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MissionController {

	private final MissionService missionService;
	private final MissionReadService missionReadService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/missions/new")
	public ApiResponse<MissionResponse> createMission(@Valid @RequestBody MissionCreateRequest request) {
		return ApiResponse.created(missionService.createMission(request.toServiceRequest()));
	}

	@PatchMapping("/api/v1/missions/{missionId}")
	public ApiResponse<MissionResponse> updateMission(@PathVariable int missionId,
		@Valid @RequestBody MissionUpdateRequest request) {
		return ApiResponse.ok(missionService.updateMission(missionId, request.toServiceRequest()));
	}

	@GetMapping("/api/v1/missions")
	public ApiResponse<MissionAggregateResponse> getMission() {
		return ApiResponse.ok(missionReadService.getMission());
	}

	@DeleteMapping("/api/v1/missions/{missionId}")
	public ApiResponse<MissionDeleteResponse> deleteMission(@PathVariable int missionId) {
		return ApiResponse.ok(missionService.deleteMission(missionId, now()));
	}
}
