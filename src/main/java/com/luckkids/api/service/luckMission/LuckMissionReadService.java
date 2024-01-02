package com.luckkids.api.service.luckMission;

import com.luckkids.api.service.luckMission.response.LuckMissionResponse;
import com.luckkids.domain.luckMission.LuckMission;
import com.luckkids.domain.luckMission.LuckMissionRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LuckMissionReadService {

    private final LuckMissionRespository luckMissionRespository;

    public List<LuckMissionResponse> getLuckMissions(){
        List<LuckMission> luckMission = luckMissionRespository.findAll();
        return luckMission.stream().map(LuckMissionResponse::of).collect(Collectors.toList());
    }
}
