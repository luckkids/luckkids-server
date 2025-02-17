package com.luckkids.api.service.user.delete;

import com.luckkids.mission.infra.MissionOutcomeRepository;
import com.luckkids.mission.infra.MissionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserMissionDeleteService {
	private final MissionOutcomeRepository missionOutcomeRepository;
	private final MissionRepository missionRepository;

	public void deleteAllByUserId(int userId) {
		missionOutcomeRepository.deleteAllByMissionUserId(userId);
		missionRepository.deleteAllByUserId(userId);
	}
}
