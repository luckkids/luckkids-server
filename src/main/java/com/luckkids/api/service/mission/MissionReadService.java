package com.luckkids.api.service.mission;

import com.luckkids.domain.misson.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MissionReadService {

    private final MissionRepository missionRepository;
    
}
