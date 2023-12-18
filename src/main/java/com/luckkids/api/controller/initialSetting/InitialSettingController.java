package com.luckkids.api.controller.initialSetting;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import com.luckkids.api.service.initialCharacter.InitialCharacterService;
import com.luckkids.api.service.initialCharacter.response.InitialCharacterRandResponse;
import com.luckkids.api.service.initialSetting.InitialSettingService;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.api.service.luckMission.LuckMissionReadService;
import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
import com.luckkids.domain.initialCharacter.InitialCharacter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/initialSetting")
public class InitialSettingController {

    private final InitialSettingService initialSettingService;
    private final InitialCharacterService initialCharacterService;
    private final LuckMissionReadService luckMissionReadService;

    @GetMapping("/luckMission")
    public ApiResponse<List<LuckMissionResponse>> getLuckMission(){
        return ApiResponse.ok(luckMissionReadService.getLuckMissions());
    }

    @GetMapping("/character")
    public ApiResponse<List<InitialCharacterRandResponse>> findAll(){
        return ApiResponse.ok(initialCharacterService.findAll());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<InitialSettingResponse> createSetting(@RequestBody @Valid InitialSettingRequest initialSettingRequest){
        return ApiResponse.created(initialSettingService.initialSetting(initialSettingRequest.toServiceRequest()));
    }
}
