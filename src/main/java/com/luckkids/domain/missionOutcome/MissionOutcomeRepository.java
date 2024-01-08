package com.luckkids.domain.missionOutcome;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MissionOutcomeRepository extends JpaRepository<MissionOutcome, Long> {

    void deleteAllByMissionIdAndMissionDate(int mission_id, LocalDate missionDate);

    @Query("SELECT COUNT(mo) " +
        "FROM MissionOutcome mo " +
        "WHERE mo.missionStatus = 'SUCCEED' " +
        "AND mo.mission.id IN (SELECT m.id FROM Mission m WHERE m.user.id = :userId)")
    int countSuccessfulMissionsByUserId(int userId);

    void deleteAllByMissionUserId(int userId);
}
