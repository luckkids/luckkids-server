package com.luckkids.api.service.mission;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		List<MissionType> missionTypes = luckkidsMissionQueryRepository.findAllGroupByMissionType();

		Map<MissionType, List<MissionResponse>> missionMap = initializeMissionMap(missionTypes);
		Map<MissionType, List<RemainingLuckkidsMissionResponse>> luckkidsMissionMap = initializeMissionMap(
			missionTypes);

		List<Mission> missions = missionRepository.findAllByUserIdAndDeletedDateIsNullOrderByMissionActiveDescAlertTimeAsc(
			userId);
		List<LuckkidsMission> luckkidsMissions = luckkidsMissionQueryRepository.findLuckkidsMissionsWithoutUserMission(
			userId);

		missions.stream()
			.map(MissionResponse::of)
			.forEach(missionResponse ->
				missionMap.get(missionResponse.getMissionType()).add(missionResponse)
			);

		luckkidsMissions.stream()
			.map(RemainingLuckkidsMissionResponse::of)
			.forEach(luckkidsMissionResponse ->
				luckkidsMissionMap.get(luckkidsMissionResponse.getMissionType()).add(luckkidsMissionResponse)
			);

		return MissionAggregateResponse.of(missionMap, luckkidsMissionMap);
	}

	private <T> Map<MissionType, List<T>> initializeMissionMap(List<MissionType> missionTypes) {
		return missionTypes.stream()
			.collect(Collectors.toMap(
				missionType -> missionType,
				missionType -> new ArrayList<>(),
				(oldValue, newValue) -> oldValue,
				LinkedHashMap::new
			));
	}
}
