package com.luckkids.api.service.mission;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.event.missionOutcome.MissionOutcomeCreateEvent;
import com.luckkids.api.event.missionOutcome.MissionOutcomeDeleteEvent;
import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionDeleteResponse;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.User;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionService {

	private final MissionRepository missionRepository;
	private final MissionReadService missionReadService;
	private final UserReadService userReadService;
	private final SecurityService securityService;
	private final ApplicationEventPublisher eventPublisher;

	public MissionResponse createMission(MissionCreateServiceRequest request) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		User user = userReadService.findByOne(userId);

		Mission mission = request.toEntity(user);
		Mission savedMission = missionRepository.save(mission);

		eventPublisher.publishEvent(new MissionOutcomeCreateEvent(this, mission));

		return MissionResponse.of(savedMission);
	}

	public MissionResponse updateMission(int missionId, MissionUpdateServiceRequest request) {
		Mission mission = missionReadService.findByOne(missionId);
		Mission updatedMission = mission.update(
			request.getMissionType(),
			request.getMissionDescription(),
			request.getMissionActive(),
			request.getAlertStatus(),
			request.getAlertTime()
		);

		return MissionResponse.of(updatedMission);
	}

	public MissionDeleteResponse deleteMission(int missionId, LocalDateTime deletedDate) {
		Mission mission = missionReadService.findByOne(missionId);
		mission.delete(deletedDate);

		eventPublisher.publishEvent(new MissionOutcomeDeleteEvent(this, mission.getId()));

		return MissionDeleteResponse.of(missionId);
	}
}
