package com.luckkids.notification.controller;

import com.luckkids.api.ApiResponse;
import com.luckkids.notification.controller.request.AlertSettingLuckMessageAlertTimeRequest;
import com.luckkids.notification.controller.request.AlertSettingRequest;
import com.luckkids.notification.controller.request.AlertSettingUpdateRequest;
import com.luckkids.notification.service.AlertSettingReadService;
import com.luckkids.notification.service.AlertSettingService;
import com.luckkids.notification.service.response.AlertSettingLuckMessageAlertTimeResponse;
import com.luckkids.notification.service.response.AlertSettingResponse;
import com.luckkids.notification.service.response.AlertSettingUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alertSetting")
public class AlertSettingController {

    private final AlertSettingReadService alertSettingReadService;
    private final AlertSettingService alertSettingService;

    @GetMapping
    public ApiResponse<AlertSettingResponse> getAlertSetting(@ModelAttribute @Valid AlertSettingRequest alertSettingRequest){
        return ApiResponse.ok(alertSettingReadService.getAlertSetting(alertSettingRequest.toServiceRequest()));
    }

    @PatchMapping("/update")
    public ApiResponse<AlertSettingUpdateResponse> updateAlertSetting(@RequestBody @Valid AlertSettingUpdateRequest alertSettingUpdateRequest){
        return ApiResponse.ok(alertSettingService.updateAlertSetting(alertSettingUpdateRequest.toServiceRequest()));
    }

    @PatchMapping("/luckMessageAlertTime/update")
    public ApiResponse<AlertSettingLuckMessageAlertTimeResponse> updateLuckMessageAlertTime(@RequestBody @Valid AlertSettingLuckMessageAlertTimeRequest alertSettingLuckMessageAlertTimeRequest){
        return ApiResponse.ok(alertSettingService.updateLuckMessageAlertTime(alertSettingLuckMessageAlertTimeRequest.toServiceRequest()));
    }
}
