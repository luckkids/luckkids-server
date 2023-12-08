package com.luckkids.api.controller.luckMission;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.service.luckMission.LuckMissionReadService;
import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/luckMission")
public class LuckMissionController {

    private final LuckMissionReadService luckMissionReadService;

    @GetMapping("/")
    public ApiResponse<List<LuckMissionResponse>> getLuckMission(){
        return ApiResponse.ok(luckMissionReadService.getLuckMissions());
    }
}
