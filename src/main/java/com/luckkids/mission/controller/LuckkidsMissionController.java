package com.luckkids.mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.mission.service.response.LuckkidsMissionListSaveResponse;
import com.luckkids.mission.controller.request.LuckkidsMissionListRequest;
import com.luckkids.mission.service.LuckkidsMissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/luckkidsMission")
public class LuckkidsMissionController {

	private final LuckkidsMissionService luckkidsMissionService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ApiResponse<LuckkidsMissionListSaveResponse> createLuckkidsMission(
		@Valid @RequestBody LuckkidsMissionListRequest luckkidsMissionListRequest) {
		return ApiResponse.created(
			luckkidsMissionService.createLuckkidsMission(luckkidsMissionListRequest.toServiceRequest()));
	}

}
