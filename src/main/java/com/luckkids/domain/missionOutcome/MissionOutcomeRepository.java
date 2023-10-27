package com.luckkids.domain.missionOutcome;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionOutcomeRepository extends JpaRepository<MissionOutcome, Long> {
}
