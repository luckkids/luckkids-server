package com.luckkids.api.service.missionOutcome;

import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionOutcomeService {

    private final MissionOutcomeRepository missionOutcomeRepository;

    private final MissionOutcomeReadService missionOutcomeReadService;

    public void createMissionOutcome(MissionOutcomeCreateServiceRequest request) {
        MissionOutcome missionOutcome = request.toEntity();
        missionOutcomeRepository.save(missionOutcome);
    }

    public Long updateMissionOutcome(Long missionOutcomeId, MissionStatus missionStatus) {
        MissionOutcome missionOutcome = missionOutcomeReadService.findByOne(missionOutcomeId);
        missionOutcome.updateMissionStatus(missionStatus);

        return missionOutcomeId;
    }
}
