package com.luckkids.domain.luckMessageHistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LuckMessageHistoryRepository extends JpaRepository<LuckMessageHistory, Integer> {
	Optional<LuckMessageHistory> findByPushDeviceIdAndPushUserId(String deviceId, int userId);

	void deleteAllByPushUserId(int userId);
}
