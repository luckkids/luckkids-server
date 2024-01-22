package com.luckkids.domain.alertSetting;

import com.luckkids.domain.push.Push;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlertSettingRepository extends JpaRepository<AlertSetting, Long> {

    Optional<AlertSetting> findByPushDeviceId(String deviceId);

//    @Query("delete from AlertSetting a where a.push.user.id = :userId")
    void deleteByPushUserId(int userId);

}
