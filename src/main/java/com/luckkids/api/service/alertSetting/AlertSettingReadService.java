package com.luckkids.api.service.alertSetting;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.alertSetting.request.AlertSettingServiceRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertSettingReadService {

    private final AlertSettingRepository alertSettingRepository;
    private final SecurityService securityService;

    public AlertSetting findOneByUserId(){
        int userId = securityService.getCurrentUserInfo().getUserId();
        return alertSettingRepository.findByUserId(userId)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.ALERT_SETTING_UNKNOWN));
    }

    public AlertSetting findOneByUserIdAndDeviceId(String deviceId){
        int userId = securityService.getCurrentUserInfo().getUserId();
        return alertSettingRepository.findByUserIdAndDeviceId(userId,deviceId)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.ALERT_SETTING_UNKNOWN));
    }

    public AlertSettingResponse getAlertSetting(AlertSettingServiceRequest alertSettingServiceRequest){
        return AlertSettingResponse.of(findOneByUserIdAndDeviceId(alertSettingServiceRequest.getDeviceId()));
    }
}
