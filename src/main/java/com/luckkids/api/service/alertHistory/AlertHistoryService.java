package com.luckkids.api.service.alertHistory;

import static com.luckkids.domain.alertHistory.AlertHistoryStatus.*;

import org.springframework.stereotype.Service;

import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.alertHistory.response.AlertHistoryStatusResponse;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertHistoryService {

	private final AlertHistoryRepository alertHistoryRepository;
	private final AlertHistoryReadService alertHistoryReadService;

	public AlertHistory createAlertHistory(AlertHistoryServiceRequest alertHistoryServiceRequest) {
		return alertHistoryRepository.save(alertHistoryServiceRequest.toEntity());
	}

	public AlertHistoryStatusResponse updateAlertHistoryStatus(Long id) {
		AlertHistory alertHistory = alertHistoryReadService.findByOne(id);
		alertHistory.updateAlertHistoryStatus(CHECKED);

		return AlertHistoryStatusResponse.of(alertHistory.getAlertHistoryStatus());
	}
}
