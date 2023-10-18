package com.luckkids.api.controller.mission;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.mission.request.MissionCreateRequest;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.mission.response.MissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MissionController {

    private final MissionService missionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/missions/new")
    public ApiResponse<MissionResponse> createMission(@Valid @RequestBody MissionCreateRequest request) {
        return ApiResponse.created(missionService.createMission(request.toServiceRequest()));
    }
}
