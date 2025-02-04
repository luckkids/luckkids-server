package com.luckkids.notification.service;

import static com.luckkids.notification.domain.alertHistory.AlertHistoryStatus.*;

import com.luckkids.notification.domain.alertHistory.AlertDestinationType;
import com.luckkids.notification.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.notification.domain.push.PushMessage;
import com.luckkids.domain.user.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.notification.service.request.AlertHistoryServiceRequest;
import com.luckkids.notification.service.response.AlertHistoryStatusResponse;
import com.luckkids.notification.domain.alertHistory.AlertHistory;
import com.luckkids.notification.infra.AlertHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertHistoryService {

	private final AlertHistoryRepository alertHistoryRepository;
	private final AlertHistoryReadService alertHistoryReadService;

	public void createWelcomeAlertHistory(User user) {
		alertHistoryRepository.save(AlertHistory.builder()
			.user(user)
			.alertDescription(PushMessage.WELCOME.getText())
			.alertHistoryStatus(AlertHistoryStatus.CHECKED)
			.alertDestinationType(AlertDestinationType.WELCOME)
			.build());
	}

	public AlertHistory createAlertHistory(AlertHistoryServiceRequest alertHistoryServiceRequest) {
		return alertHistoryRepository.save(alertHistoryServiceRequest.toEntity());
	}

	public AlertHistoryStatusResponse updateAlertHistoryStatus(Long id) {
		AlertHistory alertHistory = alertHistoryReadService.findByOne(id);
		alertHistory.updateAlertHistoryStatus(CHECKED);

		return AlertHistoryStatusResponse.of(alertHistory.getAlertHistoryStatus());
	}
}
