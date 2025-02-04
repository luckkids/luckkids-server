package com.luckkids.notification.service;

import com.luckkids.notification.service.request.AlertSettingCreateLoginServiceRequest;
import com.luckkids.notification.service.request.AlertSettingCreateServiceRequest;
import com.luckkids.notification.service.request.AlertSettingLuckMessageAlertTimeServiceRequest;
import com.luckkids.notification.service.request.AlertSettingUpdateServiceRequest;
import com.luckkids.notification.service.response.AlertSettingLuckMessageAlertTimeResponse;
import com.luckkids.notification.service.response.AlertSettingResponse;
import com.luckkids.notification.service.response.AlertSettingUpdateResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.notification.domain.alertSetting.AlertSetting;
import com.luckkids.notification.infra.AlertSettingRepository;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.notification.domain.push.Push;

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

	public AlertSettingResponse createAlertSetting(
		AlertSettingCreateLoginServiceRequest alertSettingCreateLoginServiceRequest) {
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

	public AlertSettingLuckMessageAlertTimeResponse updateLuckMessageAlertTime(
		AlertSettingLuckMessageAlertTimeServiceRequest request) {
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
