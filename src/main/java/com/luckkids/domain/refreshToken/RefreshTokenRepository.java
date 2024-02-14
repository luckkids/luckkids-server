package com.luckkids.domain.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.Ref;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteAllByUserId(int userId);
    Optional<RefreshToken> findByUserIdAndDeviceIdAndRefreshToken(int userId, String deviceId, String refreshToken);
}
