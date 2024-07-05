package com.luckkids.api.controller.alertSetting;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.alertSetting.request.AlertSettingRequest;
import com.luckkids.api.controller.alertSetting.request.AlertSettingUpdateRequest;
import com.luckkids.api.service.alertSetting.AlertSettingReadService;
import com.luckkids.api.service.alertSetting.AlertSettingService;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.alertSetting.response.AlertSettingUpdateResponse;
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
}
