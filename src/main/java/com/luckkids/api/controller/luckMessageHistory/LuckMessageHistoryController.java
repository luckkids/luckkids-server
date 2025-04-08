package com.luckkids.api.controller.luckMessageHistory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.luckMessageHistory.request.LuckMessageHistoryRequest;
import com.luckkids.api.service.luckMessageHistory.LuckMessageHistoryReadService;
import com.luckkids.api.service.luckMessageHistory.response.LuckMessageHistoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/luckMessageHistory")
public class LuckMessageHistoryController {

	private final LuckMessageHistoryReadService luckMessageHistoryReadService;

	@GetMapping
	public ApiResponse<LuckMessageHistoryResponse> findOne(
		@ModelAttribute @Valid LuckMessageHistoryRequest luckMessageHistoryRequest) {
		return ApiResponse.ok(luckMessageHistoryReadService.findOne(luckMessageHistoryRequest.toServiceRequest()));
	}
}
