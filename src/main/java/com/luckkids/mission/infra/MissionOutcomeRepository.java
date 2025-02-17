package com.luckkids.mission.infra;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckkids.mission.domain.missionOutcome.MissionOutcome;

public interface MissionOutcomeRepository extends JpaRepository<MissionOutcome, Long> {

	void deleteAllByMissionIdAndMissionDate(int missionId, LocalDate missionDate);

	void deleteAllByMissionUserId(int userId);

	MissionOutcome findByMissionIdAndMissionDate(int missionId, LocalDate missionDate);
}
