package com.luckkids.domain.alertSetting;

import com.luckkids.domain.push.Push;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlertSettingRepository extends JpaRepository<AlertSetting, Integer> {

    Optional<AlertSetting> findByPushDeviceId(String deviceId);

    void deleteByPushUserId(int userId);

}
