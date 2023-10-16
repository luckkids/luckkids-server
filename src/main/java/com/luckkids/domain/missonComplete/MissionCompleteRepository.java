package com.luckkids.domain.missonComplete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionCompleteRepository extends JpaRepository<MissionComplete, Long> {
}
