package com.luckkids.api.controller.initialSetting;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import com.luckkids.api.service.initialSetting.InitialSettingService;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/initialSetting")
public class InitialSettingController {

    private final InitialSettingService initialSettingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new")
    public ApiResponse<InitialSettingResponse> createSetting(@RequestBody @Valid InitialSettingRequest initialSettingRequest){
        return ApiResponse.created(initialSettingService.initialSetting(initialSettingRequest.toServiceRequest()));
    }
}
