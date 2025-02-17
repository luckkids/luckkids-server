package com.luckkids.api.service.user.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.notification.infra.AlertHistoryRepository;
import com.luckkids.notification.infra.AlertSettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAlertDeleteService {
	private final AlertHistoryRepository alertHistoryRepository;
	private final AlertSettingRepository alertSettingRepository;

	public void deleteAllByUserId(int userId) {
		alertHistoryRepository.deleteByUserId(userId);
		alertSettingRepository.deleteByPushUserId(userId);
	}
}
