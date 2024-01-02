package com.luckkids.api.service.user.delete;

import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAlertDeleteService implements UserDeleteService{
    private final AlertHistoryRepository alertHistoryRepository;
    private final AlertSettingRepository alertSettingRepository;

    @Override
    public void deleteAllByUserId(int userId) {
        alertHistoryRepository.deleteAllByUserId(userId);
        alertSettingRepository.deleteAllByUserId(userId);
    }
}
