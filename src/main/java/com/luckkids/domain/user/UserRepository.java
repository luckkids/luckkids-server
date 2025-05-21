package com.luckkids.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import feign.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	@Modifying(flushAutomatically = true)
	@Query("UPDATE User u " +
		"SET u.missionCount = u.missionCount + :delta " +
		"WHERE u.id = :id")
	int updateMissionCount(@Param("id") int id, @Param("delta") int delta);
}
