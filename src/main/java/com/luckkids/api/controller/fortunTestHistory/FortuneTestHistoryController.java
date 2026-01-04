package com.luckkids.api.controller.fortunTestHistory;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.fortunTestHistory.request.FortuneTestHistoryCreateRequest;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryReadService;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryService;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fortuneTest")
public class FortuneTestHistoryController {

	private final FortuneTestHistoryService fortuneTestHistoryService;
	private final FortuneTestHistoryReadService fortuneTestHistoryReadService;

	@PostMapping
	public ApiResponse<FortuneTestHistoryResponse> createFortuneTestHistory(
		@Valid @RequestBody FortuneTestHistoryCreateRequest request) {
		FortuneTestHistoryResponse response = fortuneTestHistoryService.createFortuneTestHistory(
			request.toServiceRequest());
		return ApiResponse.ok(response);
	}

	@GetMapping
	public ApiResponse<List<FortuneTestHistoryResponse>> getFortuneTestHistories() {
		List<FortuneTestHistoryResponse> response = fortuneTestHistoryService.findAll();
		return ApiResponse.ok(response);
	}
}
