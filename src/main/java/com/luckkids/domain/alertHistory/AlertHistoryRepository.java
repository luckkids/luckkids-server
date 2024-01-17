package com.luckkids.domain.alertHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    void deleteAllByUserId(int userId);
}