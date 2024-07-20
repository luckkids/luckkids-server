package com.luckkids.api.service.mission;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionDeleteResponse;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionActive;
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
	private final MissionEventService missionEventService;

	public MissionResponse createMission(MissionCreateServiceRequest request) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		User user = userReadService.findByOne(userId);

		Mission mission = request.toEntity(user);
		Mission savedMission = missionRepository.save(mission);

		missionEventService.publishMissionOutcomeCreateEvent(mission);

		return MissionResponse.of(savedMission);
	}

	public MissionResponse updateMission(int missionId, MissionUpdateServiceRequest request) {
		Mission mission = missionReadService.findByOne(missionId);
		MissionActive currentMissionActive = mission.getMissionActive();

		Mission updatedMission = mission.update(
			request.getMissionType(),
			request.getMissionDescription(),
			request.getMissionActive(),
			request.getAlertStatus(),
			request.getAlertTime()
		);

		missionEventService.handleMissionStateTransition(mission, currentMissionActive, request.getMissionActive());

		return MissionResponse.of(updatedMission);
	}

	public MissionDeleteResponse deleteMission(int missionId, LocalDateTime deletedDate) {
		Mission mission = missionReadService.findByOne(missionId);
		mission.delete(deletedDate);

		missionEventService.publishMissionOutcomeDeleteEvent(mission);

		return MissionDeleteResponse.of(missionId);
	}
}
