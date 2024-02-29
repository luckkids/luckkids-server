package com.luckkids.domain.alertHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {

    void deleteByPushUserId(int userId);

    @Query(value = "SELECT * FROM alert_history WHERE device_id = :deviceId", nativeQuery = true)
    AlertHistory findByDeviceId(String deviceId);
}