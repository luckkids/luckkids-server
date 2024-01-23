package com.luckkids.domain.alertHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    void deleteByPushUserId(int userId);
}