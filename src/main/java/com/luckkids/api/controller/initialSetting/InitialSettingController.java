package com.luckkids.api.controller.initialSetting;

import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import com.luckkids.api.service.initialSetting.InitialSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/initialSetting")
public class InitialSettingController {

    private final InitialSettingService initialSettingService;

    @PostMapping("/new")
    public void createSetting(@RequestBody @Valid InitialSettingRequest initialSettingRequest){
        initialSettingService.initialSetting(initialSettingRequest.toServiceRequest());
    }
}
