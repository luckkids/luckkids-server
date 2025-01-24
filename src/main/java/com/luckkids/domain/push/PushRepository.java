package com.luckkids.domain.push;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PushRepository extends JpaRepository<Push, Integer> {
    void deleteAllByUserId(int userId);
    Optional<Push> findByDeviceIdAndUserId(String deviceId, int userId);
    
}
