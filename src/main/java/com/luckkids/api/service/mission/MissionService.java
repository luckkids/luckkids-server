package com.luckkids.api.service.mission;

import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    public MissionResponse createMission(MissionCreateServiceRequest request) {
        String loginUserEmail = "test1234@naver.com"; // security 완성되면 수정필요 !
        User user = userRepository.findByEmail(loginUserEmail);

        Mission mission = request.toEntity(user);
        Mission savedMission = missionRepository.save(mission);

        return MissionResponse.of(savedMission);
    }
}
