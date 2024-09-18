package com.luckkids.api.controller.initialSetting;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import com.luckkids.api.service.initialSetting.InitialSettingService;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.api.service.luckkidsCharacter.LuckkidsCharacterReadService;
import com.luckkids.api.service.luckkidsCharacter.response.LuckkidsCharacterRandResponse;
import com.luckkids.api.service.luckkidsMission.LuckkidsMissionReadService;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/initialSetting")
public class InitialSettingController {

	private final InitialSettingService initialSettingService;
	private final LuckkidsCharacterReadService luckkidsCharacterReadService;
	private final LuckkidsMissionReadService luckkidsMissionReadService;

	@GetMapping("/luckMission")
	public ApiResponse<List<LuckkidsMissionResponse>> getLuckMission() {
		return ApiResponse.ok(luckkidsMissionReadService.getLuckMissions());
	}

	@GetMapping("/character")
	public ApiResponse<LuckkidsCharacterRandResponse> findRandomCharacterLevel1() {
		return ApiResponse.ok(luckkidsCharacterReadService.findRandomCharacterLevel1());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("")
	public ApiResponse<InitialSettingResponse> createSetting(
		@RequestBody @Valid InitialSettingRequest initialSettingRequest) {
		return ApiResponse.created(initialSettingService.initialSetting(initialSettingRequest.toServiceRequest()));
	}
}
