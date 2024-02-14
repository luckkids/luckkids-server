package com.luckkids.domain.missionOutcome;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MissionOutcomeRepository extends JpaRepository<MissionOutcome, Long> {

    void deleteAllByMissionIdAndMissionDate(int mission_id, LocalDate missionDate);

    void deleteAllByMissionUserId(int userId);
}
