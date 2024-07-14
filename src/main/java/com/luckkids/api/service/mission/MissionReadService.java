package com.luckkids.api.service.mission;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.mission.response.MissionAggregateResponse;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.mission.response.RemainingLuckkidsMissionResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionQueryRepository;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MissionReadService {

	private final MissionRepository missionRepository;
	private final LuckkidsMissionQueryRepository luckkidsMissionQueryRepository;

	private final SecurityService securityService;

	public Mission findByOne(int id) {
		return missionRepository.findByIdAndDeletedDateIsNull(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 미션은 없습니다. id = " + id));
	}

	public MissionAggregateResponse getMission() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();

		List<Mission> missions = missionRepository.findAllByUserIdAndDeletedDateIsNull(userId);
		List<LuckkidsMission> luckkidsMissions = luckkidsMissionQueryRepository.findLuckkidsMissionsWithoutUserMission(
			userId);

		Map<MissionType, List<MissionResponse>> missionMap = missions.stream()
			.map(MissionResponse::of)
			.collect(groupingBy(MissionResponse::getMissionType));

		Map<MissionType, List<RemainingLuckkidsMissionResponse>> luckkidsMisisonMap = luckkidsMissions.stream()
			.map(RemainingLuckkidsMissionResponse::of)
			.collect(groupingBy(RemainingLuckkidsMissionResponse::getMissionType));

		return MissionAggregateResponse.of(missionMap, luckkidsMisisonMap);
	}
}
