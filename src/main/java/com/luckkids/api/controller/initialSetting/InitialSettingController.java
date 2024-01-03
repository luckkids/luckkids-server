package com.luckkids.api.controller.initialSetting;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import com.luckkids.api.service.luckkidsCharacter.InitialCharacterService;
import com.luckkids.api.service.luckkidsCharacter.response.InitialCharacterRandResponse;
import com.luckkids.api.service.initialSetting.InitialSettingService;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.api.service.luckMission.LuckMissionReadService;
import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
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
    public ApiResponse<List<InitialCharacterRandResponse>> findBylevel1(){
        return ApiResponse.ok(initialCharacterService.findAllByCharacterIdLevel1());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<InitialSettingResponse> createSetting(@RequestBody @Valid InitialSettingRequest initialSettingRequest){
        return ApiResponse.created(initialSettingService.initialSetting(initialSettingRequest.toServiceRequest()));
    }
}
