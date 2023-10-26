package com.luckkids.api.service.missionOutcome;

import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MissionOutcomeReadService {

    private final MissionOutcomeRepository missionOutcomeRepository;

    public MissionOutcome findByOne(Long id) {
        return missionOutcomeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 미션결과는 없습니다. id = " + id));
    }
}
