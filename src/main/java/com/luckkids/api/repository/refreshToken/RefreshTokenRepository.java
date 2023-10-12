package com.luckkids.api.repository.refreshToken;

import com.luckkids.domain.refreshToken.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public RefreshToken findByUserId(Long userId);
}
