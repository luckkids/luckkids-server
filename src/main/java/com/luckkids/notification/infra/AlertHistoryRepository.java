package com.luckkids.notification.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.luckkids.notification.domain.alertHistory.AlertHistory;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {

	void deleteByUserId(int userId);

	List<AlertHistory> findByUserId(int userId);
}
