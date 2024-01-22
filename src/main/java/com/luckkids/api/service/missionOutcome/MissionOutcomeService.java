package com.luckkids.api.service.missionOutcome;

import com.luckkids.api.event.user.UserMissionCountUpdateEvent;
import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionOutcomeService {

    private final MissionOutcomeRepository missionOutcomeRepository;

    private final MissionOutcomeReadService missionOutcomeReadService;

    private final ApplicationEventPublisher eventPublisher;

    public void createMissionOutcome(MissionOutcomeCreateServiceRequest request) {
        MissionOutcome missionOutcome = request.toEntity();
        missionOutcomeRepository.save(missionOutcome);
    }

    public int updateMissionOutcome(Long missionOutcomeId, MissionStatus missionStatus) {
        MissionOutcome missionOutcome = missionOutcomeReadService.findByOne(missionOutcomeId);
        missionOutcome.updateMissionStatus(missionStatus);

        eventPublisher.publishEvent(new UserMissionCountUpdateEvent(this, missionStatus));

        return missionOutcomeReadService.countUserSuccessfulMissions();
    }

    public void deleteMissionOutcome(int missionId, LocalDate missionDate) {
        missionOutcomeRepository.deleteAllByMissionIdAndMissionDate(missionId, missionDate);
    }
}
