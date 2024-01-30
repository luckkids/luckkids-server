package com.luckkids.api.service.luckMission;

import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LuckMissionReadService {

    private final LuckkidsMissionRepository luckkidsMissionRepository;

    public List<LuckMissionResponse> getLuckMissions() {
        List<LuckkidsMission> luckMission = luckkidsMissionRepository.findAll();
        return luckMission.stream().map(LuckMissionResponse::of).collect(Collectors.toList());
    }
}

