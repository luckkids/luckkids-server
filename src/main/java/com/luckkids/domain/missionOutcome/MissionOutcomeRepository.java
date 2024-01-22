package com.luckkids.domain.missionOutcome;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface MissionOutcomeRepository extends JpaRepository<MissionOutcome, Long> {

    @Query("SELECT COUNT(mo) " +
        "FROM MissionOutcome mo " +
        "WHERE mo.missionStatus = 'SUCCEED' " +
        "AND mo.mission.id IN (SELECT m.id FROM Mission m WHERE m.user.id = :userId)")
    int countSuccessfulMissionsByUserId(int userId);

    void deleteAllByMissionIdAndMissionDate(int mission_id, LocalDate missionDate);

    void deleteAllByMissionUserId(int userId);
}
