package com.luckkids.api.service.mission;

import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;

    private final MissionReadService missionReadService;
    private final UserReadService userReadService;
    private final SecurityService securityService;

    public MissionResponse createMission(MissionCreateServiceRequest request) {
        int userId = securityService.getCurrentUserId();
        User user = userReadService.findByOne(userId);

        Mission mission = request.toEntity(user);
        Mission savedMission = missionRepository.save(mission);

        return MissionResponse.of(savedMission);
    }

    public MissionResponse updateMission(int missionId, MissionUpdateServiceRequest request) {
        Mission mission = missionReadService.findByOne(missionId);
        Mission updatedMission = mission.update(
            request.getMissionDescription(),
            request.getAlertStatus(),
            request.getAlertTime()
        );

        return MissionResponse.of(updatedMission);
    }
}
