package com.luckkids.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.notification.service.request.AlertSettingServiceRequest;
import com.luckkids.notification.service.response.AlertSettingResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.notification.domain.alertSetting.AlertSetting;
import com.luckkids.notification.infra.AlertSettingRepository;

import lombok.RequiredArgsConstructor;

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

	public List<AlertSetting> findAllByUserId(int userId) {
		return alertSettingRepository.findByPushUserId(userId);
	}
}
