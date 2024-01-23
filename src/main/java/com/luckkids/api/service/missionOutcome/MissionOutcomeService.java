package com.luckkids.api.service.missionOutcome;

import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;
import com.luckkids.api.service.user.UserService;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionOutcomeService {

    private final MissionOutcomeRepository missionOutcomeRepository;

    private final MissionOutcomeReadService missionOutcomeReadService;
    private final UserService userService;

    public void createMissionOutcome(MissionOutcomeCreateServiceRequest request) {
        MissionOutcome missionOutcome = request.toEntity();
        missionOutcomeRepository.save(missionOutcome);
    }

    public int updateMissionOutcome(Long missionOutcomeId, MissionStatus missionStatus) {
        MissionOutcome missionOutcome = missionOutcomeReadService.findByOne(missionOutcomeId);
        if (missionOutcome.getMissionStatus().equals(missionStatus)) {
            throw new IllegalArgumentException("미션 상태가 기존과 같습니다.");
        }
        missionOutcome.updateMissionStatus(missionStatus);

        return userService.updateMissionCount(missionStatus);
    }

    public void deleteMissionOutcome(int missionId, LocalDate missionDate) {
        missionOutcomeRepository.deleteAllByMissionIdAndMissionDate(missionId, missionDate);
    }
}
