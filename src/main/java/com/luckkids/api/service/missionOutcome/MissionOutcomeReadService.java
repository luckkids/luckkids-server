package com.luckkids.api.service.missionOutcome;

import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeQueryRepository;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MissionOutcomeReadService {

    private final MissionOutcomeRepository missionOutcomeRepository;
    private final MissionOutcomeQueryRepository missionOutcomeQueryRepository;

    public MissionOutcome findByOne(Long id) {
        return missionOutcomeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 미션결과는 없습니다. id = " + id));
    }

    public List<MissionOutcomeResponse> getMissionDetailListForStatus(Optional<MissionStatus> missionStatus, int userId, LocalDate missionDate) {
        return missionOutcomeQueryRepository.findMissionDetailsByStatus(missionStatus, userId, missionDate)
            .stream()
            .map(MissionOutcomeDetailDto::toMissionOutcomeResponse)
            .toList();
    }
}
