package com.luckkids.api.service.alertSetting;

import com.luckkids.api.service.alertSetting.request.AlertSettingCreateServiceRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.alertSetting.response.AlertSettingUpdateResponse;
import com.luckkids.api.service.push.PushReadService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.User;
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
    private final UserReadService userReadService;
    private final PushReadService pushReadService;

    public AlertSettingUpdateResponse updateAlertSetting(AlertSettingUpdateServiceRequest request) {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        AlertSetting alertSetting = alertSettingReadService.findOneByDeviceId(request.getDeviceId());
        alertSetting.update(request.getAlertType(), request.getAlertStatus());
        return AlertSettingUpdateResponse.of(alertSetting);
    }

    public AlertSettingResponse createAlertSetting(AlertSettingCreateServiceRequest alertSettingCreateServiceRequest) {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        Push push = pushReadService.findByDeviceIdAndUser(alertSettingCreateServiceRequest.getDeviceId(), userId);
        AlertSetting savedAlertSetting = alertSettingRepository.save(AlertSetting.of(push, alertSettingCreateServiceRequest.getAlertStatus()));
        return AlertSettingResponse.of(savedAlertSetting);
    }
}
