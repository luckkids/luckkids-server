package com.luckkids.domain.alertSetting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertSettingRepository extends JpaRepository<AlertSetting, Integer> {

    Optional<AlertSetting> findByUserId(int userId);

    Optional<AlertSetting> findByUserIdAndDeviceId(int userId, String deviceId);

    void deleteAllByUserId(int userId);

}
