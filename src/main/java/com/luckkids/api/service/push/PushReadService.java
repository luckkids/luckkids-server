package com.luckkids.api.service.push;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushReadService {

    private final PushRepository pushRepository;

    public Push findByDeviceIdAndUser(String deviceId, int userId){
        return pushRepository.findByDeviceIdAndUserId(deviceId,userId)
            .orElseThrow(() -> new IllegalArgumentException("Push가 존재하지 않습니다."));
    }

    public List<Push> findAllByAlertType(AlertType alertType){
        return switch (alertType) {
            case LUCK -> pushRepository.findByAlertSetting_LuckMessage(AlertStatus.CHECKED);
            case MISSION -> pushRepository.findByAlertSetting_Mission(AlertStatus.CHECKED);
            case NOTICE -> pushRepository.findByAlertSetting_Notice(AlertStatus.CHECKED);
            default -> throw new LuckKidsException(ErrorCode.LUCKKIDS_CHARACTER_UNKNOWN);
        };
    }
}
