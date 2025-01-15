package com.luckkids.api.controller.luckMessageHistory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.luckMessageHistory.request.LuckMessageHistoryRequest;
import com.luckkids.api.service.luckMessageHistory.LuckMessageHistoryReadService;
import com.luckkids.api.service.luckMessageHistory.response.LuckMessageHistoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LuckMessageHistoryController {

	private final LuckMessageHistoryReadService luckMessageHistoryReadService;

	@GetMapping
	public ApiResponse<LuckMessageHistoryResponse> findOne(
		@RequestBody @Valid LuckMessageHistoryRequest luckMessageHistoryRequest) {
		return ApiResponse.ok(luckMessageHistoryReadService.findOne(luckMessageHistoryRequest.toServiceRequest()));
	}
}
