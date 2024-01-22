package com.luckkids.domain.alertHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
//    @Query("delete from AlertHistory a where a.push.user.id = :userId")
    void deleteByPushUserId(int userId);
}