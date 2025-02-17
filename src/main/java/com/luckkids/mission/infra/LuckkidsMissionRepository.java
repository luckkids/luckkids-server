package com.luckkids.mission.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckkids.mission.domain.luckkidsMission.LuckkidsMission;

public interface LuckkidsMissionRepository extends JpaRepository<LuckkidsMission, Integer> {
}
