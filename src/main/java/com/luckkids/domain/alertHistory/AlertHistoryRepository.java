package com.luckkids.domain.alertHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {

	void deleteByUserId(int userId);

	List<AlertHistory> findByUserId(int userId);
}
