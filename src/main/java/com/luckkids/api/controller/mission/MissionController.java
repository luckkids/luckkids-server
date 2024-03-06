package com.luckkids.api.controller.mission;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.mission.request.MissionCreateRequest;
import com.luckkids.api.controller.mission.request.MissionUpdateRequest;
import com.luckkids.api.service.mission.MissionReadService;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.mission.response.MissionDeleteResponse;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.domain.misson.MissionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
public class MissionController {

    private final MissionService missionService;
    private final MissionReadService missionReadService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/missions/new")
    public ApiResponse<MissionResponse> createMission(@Valid @RequestBody MissionCreateRequest request) {
        return ApiResponse.created(missionService.createMission(request.toServiceRequest()));
    }

    @PatchMapping("/api/v1/missions/{missionId}")
    public ApiResponse<MissionResponse> updateMission(@PathVariable int missionId,
                                                      @Valid @RequestBody MissionUpdateRequest request) {
        return ApiResponse.ok(missionService.updateMission(missionId, request.toServiceRequest()));
    }

    @GetMapping("/api/v1/missions")
    public ApiResponse<Map<MissionType, List<MissionResponse>>> getMission() {
        return ApiResponse.ok(missionReadService.getMission());
    }

    @DeleteMapping("/api/v1/missions/{missionId}")
    public ApiResponse<MissionDeleteResponse> deleteMission(@PathVariable int missionId) {
        return ApiResponse.ok(missionService.deleteMission(missionId, now()));
    }
}
