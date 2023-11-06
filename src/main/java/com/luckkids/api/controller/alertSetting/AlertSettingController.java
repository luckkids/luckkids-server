package com.luckkids.api.controller.alertSetting;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.alertSetting.request.AlertSettingUpdateRequest;
import com.luckkids.api.service.alertSetting.AlertSettingReadService;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.security.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alertSetting")
public class AlertSettingController {

    private final SecurityService securityService;
    private final AlertSettingReadService alertSettingReadService;

    @GetMapping("/find")
    public ApiResponse<AlertSettingResponse> find(){
        int userId = securityService.getCurrentUserInfo().getUserId();
        return ApiResponse.ok(alertSettingReadService.find(userId));
    }

    @PatchMapping("/update")
    public ApiResponse<AlertSettingResponse> update(@RequestBody @Valid AlertSettingUpdateRequest alertSettingUpdateRequest){
        int userId = securityService.getCurrentUserInfo().getUserId();
        return ApiResponse.ok(alertSettingReadService.update(alertSettingUpdateRequest.toServiceRequest(), userId));
    }
}
