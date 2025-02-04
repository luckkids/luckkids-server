package com.luckkids.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.page.request.PageInfoRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.notification.service.AlertHistoryReadService;
import com.luckkids.notification.service.AlertHistoryService;
import com.luckkids.notification.service.response.AlertHistoryResponse;
import com.luckkids.notification.service.response.AlertHistoryStatusResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AlertHistoryController {

	private final AlertHistoryService alertHistoryService;
	private final AlertHistoryReadService alertHistoryReadService;

	@GetMapping("/api/v1/alertHistories")
	public ApiResponse<PageCustom<AlertHistoryResponse>> getAlertHistory(@ModelAttribute PageInfoRequest request) {
		return ApiResponse.ok(alertHistoryReadService.getAlertHistory(request.toServiceRequest()));
	}

	@PatchMapping("/api/v1/alertHistories/{id}")
	public ApiResponse<AlertHistoryStatusResponse> updateAlertHistoryStatus(@PathVariable Long id) {
		return ApiResponse.ok(alertHistoryService.updateAlertHistoryStatus(id));
	}
}
