package com.luckkids.domain.push;

import com.luckkids.domain.misson.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PushRepository extends JpaRepository<Push, String> {
    void deleteAllByUserId(int userId);
    Optional<Push> findByDeviceIdAndUserId(String deviceId, int userId);
    List<Push> findByAlertSetting_Notice(AlertStatus alertStatus);
    List<Push> findByAlertSetting_Mission(AlertStatus alertStatus);
    List<Push> findByAlertSetting_LuckMessage(AlertStatus alertStatus);
}
