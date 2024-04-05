package com.luckkids.api.service.mission;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionQueryRepository;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.misson.projection.LuckkidsUserMissionDto;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MissionReadService {

	private final MissionRepository missionRepository;
	private final MissionQueryRepository missionQueryRepository;

	private final SecurityService securityService;

	public Mission findByOne(int id) {
		return missionRepository.findByIdAndDeletedDateIsNull(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 미션은 없습니다. id = " + id));
	}

	public Map<MissionType, List<LuckkidsUserMissionDto>> getMission() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		List<LuckkidsUserMissionDto> missions = missionQueryRepository.findLuckkidsUserMissionsByUserId(userId);

		return missions.stream()
			.collect(groupingBy(LuckkidsUserMissionDto::missionType));
	}
}
