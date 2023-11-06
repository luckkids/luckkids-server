package com.luckkids.api.service.alertSetting;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertSettingReadService {

    private final AlertSettingRepository alertSettingRepository;
    private final UserRepository userRepository;

    public AlertSettingResponse find(int userId){
        AlertSetting alertSetting = alertSettingRepository.findByUserId(userId);
        return AlertSettingResponse.of(alertSetting);
    }

    public AlertSettingResponse update(AlertSettingUpdateServiceRequest request, int userId){
        AlertSetting alertSetting = alertSettingRepository.findByUserId(userId);
        alertSetting.update(request.getAlertType(), request.getAlertStatus());
        return AlertSettingResponse.of(alertSetting);
    }

    public AlertSettingResponse save(int userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.USER_UNKNOWN));
        AlertSetting alertSetting = AlertSetting.of(user);
        AlertSetting savedAlertSetting = alertSettingRepository.save(alertSetting);
        return AlertSettingResponse.of(savedAlertSetting);
    }
}
