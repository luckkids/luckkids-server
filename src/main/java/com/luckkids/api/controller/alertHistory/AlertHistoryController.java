package com.luckkids.api.controller.alertHistory;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.alertHistory.request.AlertHistoryDeviceIdRequest;
import com.luckkids.api.service.alertHistory.AlertHistoryReadService;
import com.luckkids.api.service.alertHistory.AlertHistoryService;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import com.luckkids.api.service.alertHistory.response.AlertHistoryStatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AlertHistoryController {

    private final AlertHistoryService alertHistoryService;
    private final AlertHistoryReadService alertHistoryReadService;

    @GetMapping("/api/v1/alertHistories")
    public ApiResponse<AlertHistoryResponse> getAlertHistory(@ModelAttribute @Valid AlertHistoryDeviceIdRequest request) {
        return ApiResponse.ok(alertHistoryReadService.getAlertHistory(request.toServiceRequest()));
    }

    @PatchMapping("/api/v1/alertHistories/{id}")
    public ApiResponse<AlertHistoryStatusResponse> updateAlertHistoryStatus(@PathVariable Long id) {
        return ApiResponse.ok(alertHistoryService.updateAlertHistoryStatus(id));
    }
}
