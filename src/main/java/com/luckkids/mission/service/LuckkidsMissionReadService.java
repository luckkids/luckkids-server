package com.luckkids.mission.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.mission.service.response.LuckkidsMissionResponse;
import com.luckkids.mission.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.mission.infra.LuckkidsMissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LuckkidsMissionReadService {

	private final LuckkidsMissionRepository luckkidsMissionRepository;

	public List<LuckkidsMissionResponse> getLuckMissions() {
		List<LuckkidsMission> luckMission = luckkidsMissionRepository.findAll();
		return luckMission.stream().map(LuckkidsMissionResponse::of).collect(Collectors.toList());
	}
}

