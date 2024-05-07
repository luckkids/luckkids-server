package com.luckkids.api.controller.initialSetting;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.initialSetting.request.InitialSettingRequest;
import com.luckkids.api.service.luckkidsMission.LuckkidsMissionReadService;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionResponse;
import com.luckkids.api.service.initialSetting.InitialSettingService;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.api.service.luckkidsCharacter.LuckkidsCharacterReadService;
import com.luckkids.api.service.luckkidsCharacter.response.LuckCharacterRandResponse;
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
    private final LuckkidsCharacterReadService luckkidsCharacterReadService;
    private final LuckkidsMissionReadService luckkidsMissionReadService;

    @GetMapping("/luckMission")
    public ApiResponse<List<LuckkidsMissionResponse>> getLuckMission() {
        return ApiResponse.ok(luckkidsMissionReadService.getLuckMissions());
    }

    @GetMapping("/character")
    public ApiResponse<LuckCharacterRandResponse> findRandomCharacterLevel1() {
        return ApiResponse.ok(luckkidsCharacterReadService.findRandomCharacterLevel1());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<InitialSettingResponse> createSetting(@RequestBody @Valid InitialSettingRequest initialSettingRequest) {
        return ApiResponse.created(initialSettingService.initialSetting(initialSettingRequest.toServiceRequest()));
    }
}
