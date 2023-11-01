package com.luckkids.domain.missionOutcome;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MissionOutcomeRepository extends JpaRepository<MissionOutcome, Long> {

    void deleteAllByMissionIdAndMissionDate(int mission_id, LocalDate missionDate);
}
