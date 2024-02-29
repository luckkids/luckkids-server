package com.luckkids.api.controller.alertHistory;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.alertHistory.request.AlertHistoryDeviceIdRequest;
import com.luckkids.api.service.alertHistory.AlertHistoryReadService;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AlertHistoryController {

    private final AlertHistoryReadService alertHistoryReadService;

    @GetMapping("/api/v1/alertHistories")
    public ApiResponse<AlertHistoryResponse> getAlertHistory(@ModelAttribute @Valid AlertHistoryDeviceIdRequest request) {
        return ApiResponse.ok(alertHistoryReadService.getAlertHistory(request.toServiceRequest()));
    }
}
