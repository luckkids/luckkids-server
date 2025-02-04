package com.luckkids.api.service.user.delete;

import com.luckkids.notification.infra.PushRepository;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserTokenDeleteService {
    private final PushRepository pushRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void deleteAllByUserId(int userId){
        pushRepository.deleteAllByUserId(userId);
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
