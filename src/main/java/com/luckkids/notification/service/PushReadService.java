package com.luckkids.notification.service;

import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.notification.domain.push.Push;
import com.luckkids.notification.infra.PushQueryRepository;
import com.luckkids.notification.infra.PushRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushReadService {

	private final PushRepository pushRepository;
	private final PushQueryRepository pushQueryRepository;

	public Push findByDeviceIdAndUser(String deviceId, int userId) {
		return pushRepository.findByDeviceIdAndUserId(deviceId, userId)
			.orElseThrow(() -> new IllegalArgumentException("Push가 존재하지 않습니다."));
	}

	public List<Push> findAllByAlertType(AlertType alertType) {
		return pushQueryRepository.findAllByAlertType(alertType);
	}
}
