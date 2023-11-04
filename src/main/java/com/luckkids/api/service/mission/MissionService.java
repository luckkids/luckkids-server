package com.luckkids.api.service.mission;

import com.luckkids.api.event.missionOutcome.MissionOutcomeCreateEvent;
import com.luckkids.api.event.missionOutcome.MissionOutcomeDeleteEvent;
import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;

    private final MissionReadService missionReadService;
    private final UserReadService userReadService;

    private final ApplicationEventPublisher eventPublisher;

    public MissionResponse createMission(MissionCreateServiceRequest request, int userId) {
        User user = userReadService.findByOne(userId);

        Mission mission = request.toEntity(user);
        Mission savedMission = missionRepository.save(mission);

        eventPublisher.publishEvent(new MissionOutcomeCreateEvent(this, mission));

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

    public int deleteMission(int missionId, LocalDateTime deletedDate) {
        Mission mission = missionReadService.findByOne(missionId);
        mission.delete(deletedDate);

        eventPublisher.publishEvent(new MissionOutcomeDeleteEvent(this, mission.getId()));

        return missionId;
    }
}
