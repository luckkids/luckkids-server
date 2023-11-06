package com.luckkids.domain.alertSetting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertSettingRepository extends JpaRepository<AlertSetting, Integer> {

    public AlertSetting findByUserId(int userId);
}
