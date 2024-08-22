package com.luckkids.api.service.alertSetting;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.alertSetting.request.AlertSettingServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.alertSetting.AlertType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertSettingReadService {

    private final AlertSettingRepository alertSettingRepository;
    private final SecurityService securityService;

    public AlertSetting findOneByDeviceIdAndUserId(String deviceId) {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        return alertSettingRepository.findByPushDeviceIdAndPushUserId(deviceId, userId)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.ALERT_SETTING_UNKNOWN));
    }

    public AlertSettingResponse getAlertSetting(AlertSettingServiceRequest alertSettingServiceRequest) {
        return AlertSettingResponse.of(findOneByDeviceIdAndUserId(alertSettingServiceRequest.getDeviceId()));
    }

    public List<AlertSetting> findAllByUserId(int userId){
        return alertSettingRepository.findByPushUserId(userId);
    }
}
