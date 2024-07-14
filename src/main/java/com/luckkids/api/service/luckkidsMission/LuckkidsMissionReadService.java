package com.luckkids.api.service.luckkidsMission;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionResponse;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;

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

