package com.luckkids.api.service.alertSetting;

import com.luckkids.api.service.alertSetting.request.AlertSettingUpdateServiceRequest;
import com.luckkids.api.service.alertSetting.response.AlertSettingResponse;
import com.luckkids.api.service.alertSetting.response.AlertSettingUpdateResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.alertSetting.AlertSettingRepository;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertSettingService {

    private final AlertSettingRepository alertSettingRepository;
    private final AlertSettingReadService alertSettingReadService;
    private final SecurityService securityService;
    private final UserReadService userReadService;

    public AlertSettingUpdateResponse updateAlertSetting(AlertSettingUpdateServiceRequest request){
        AlertSetting alertSetting = alertSettingReadService.findOneByUserIdAndDeviceId(request.getDeviceId());
        alertSetting.update(request.getAlertType(), request.getAlertStatus());
        return AlertSettingUpdateResponse.of(alertSetting);
    }

    public AlertSettingResponse createAlertSetting(){
        int userId = securityService.getCurrentUserInfo().getUserId();
        User user = userReadService.findByOne(userId);
        AlertSetting savedAlertSetting = alertSettingRepository.save(AlertSetting.of(user));
        return AlertSettingResponse.of(savedAlertSetting);
    }
}
