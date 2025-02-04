package com.luckkids.notification.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.luckkids.notification.domain.push.Push;

public interface PushRepository extends JpaRepository<Push, Integer> {
    void deleteAllByUserId(int userId);
    Optional<Push> findByDeviceIdAndUserId(String deviceId, int userId);
    
}
