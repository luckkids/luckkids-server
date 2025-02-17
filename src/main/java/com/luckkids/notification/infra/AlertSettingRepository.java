package com.luckkids.notification.infra;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckkids.notification.domain.alertSetting.AlertSetting;

public interface AlertSettingRepository extends JpaRepository<AlertSetting, Integer> {

	Optional<AlertSetting> findByPushDeviceIdAndPushUserId(String deviceId, int userId);

	List<AlertSetting> findByPushUserId(int userId);

	void deleteByPushUserId(int userId);

}
