package com.luckkids.api.service.mission;

import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MissionReadService {

    private final MissionRepository missionRepository;

    private final SecurityService securityService;

    public Mission findByOne(int id) {
        return missionRepository.findByIdAndDeletedDateIsNull(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 미션은 없습니다. id = " + id));
    }

    public List<MissionResponse> getMission() {
        int userId = securityService.getCurrentUserInfo().getUserId();
        List<Mission> missions = missionRepository.findAllByUserIdAndDeletedDateIsNull(userId);

        return missions.stream().map(MissionResponse::of).collect(Collectors.toList());
    }
}
