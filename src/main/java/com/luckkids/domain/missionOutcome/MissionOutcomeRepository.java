package com.luckkids.domain.missionOutcome;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionOutcomeRepository extends JpaRepository<MissionOutcome, Long> {

	void deleteAllByMissionIdAndMissionDate(int missionId, LocalDate missionDate);

	void deleteAllByMissionUserId(int userId);
}
