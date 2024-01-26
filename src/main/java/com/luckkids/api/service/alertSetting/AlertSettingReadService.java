package com.luckkids.api.service.alertSetting;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.alertSetting.request.AlertSettingServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.push.PushReadService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.push.Push;
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

    public AlertSetting findOneByDeviceId(String deviceId) {
        return alertSettingRepository.findByPushDeviceId(deviceId)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.ALERT_SETTING_UNKNOWN));
    }

    public AlertSettingResponse getAlertSetting(AlertSettingServiceRequest alertSettingServiceRequest) {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        return AlertSettingResponse.of(findOneByDeviceId(alertSettingServiceRequest.getDeviceId()));
    }
}
