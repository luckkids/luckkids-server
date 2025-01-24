package com.luckkids.api.service.alertSetting;

import com.luckkids.api.service.alertSetting.request.AlertSettingCreateLoginServiceRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingCreateServiceRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingLuckMessageAlertTimeServiceRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingLuckMessageAlertTimeResponse;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.alertSetting.response.AlertSettingUpdateResponse;
import com.luckkids.api.service.push.PushReadService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.domain.push.Push;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertSettingService {

    private final AlertSettingRepository alertSettingRepository;
    private final AlertSettingReadService alertSettingReadService;
    private final SecurityService securityService;
    private final PushReadService pushReadService;

    public AlertSettingUpdateResponse updateAlertSetting(AlertSettingUpdateServiceRequest request) {
        AlertSetting alertSetting = alertSettingReadService.findOneByDeviceIdAndUserId(request.getDeviceId());
        alertSetting.update(request.getAlertType(), request.getAlertStatus());
        return AlertSettingUpdateResponse.of(alertSetting);
    }

    public AlertSettingResponse createAlertSetting(AlertSettingCreateLoginServiceRequest alertSettingCreateLoginServiceRequest) {
        return createAlertSetting(
                alertSettingCreateLoginServiceRequest.getDeviceId(),
                alertSettingCreateLoginServiceRequest.getUserId(),
                alertSettingCreateLoginServiceRequest.getAlertStatus()
        );
    }

    public AlertSettingResponse createAlertSetting(AlertSettingCreateServiceRequest alertSettingCreateServiceRequest) {
        return createAlertSetting(
                alertSettingCreateServiceRequest.getDeviceId(),
                securityService.getCurrentLoginUserInfo().getUserId(),
                alertSettingCreateServiceRequest.getAlertStatus()
        );
    }

    public AlertSettingLuckMessageAlertTimeResponse updateLuckMessageAlertTime(AlertSettingLuckMessageAlertTimeServiceRequest request) {
        AlertSetting alertSetting = alertSettingReadService.findOneByDeviceIdAndUserId(request.getDeviceId());
        alertSetting.updateLuckMessageAlertTime(request.getLuckMessageAlertTime());
        return AlertSettingLuckMessageAlertTimeResponse.of(alertSetting);
    }

    private AlertSettingResponse createAlertSetting(String deviceId, int userId, AlertStatus alertStatus) {
        Push push = pushReadService.findByDeviceIdAndUser(deviceId, userId);
        AlertSetting savedAlertSetting = alertSettingRepository.save(AlertSetting.of(push, alertStatus));
        return AlertSettingResponse.of(savedAlertSetting);
    }
}
