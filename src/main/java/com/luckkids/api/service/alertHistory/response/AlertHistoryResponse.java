package com.luckkids.api.service.alertHistory.response;

import java.time.LocalDateTime;

import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AlertHistoryResponse {

	private Long id;
	private String alertDescription;
	private AlertHistoryStatus alertHistoryStatus;
	private AlertDestinationType alertDestinationType;
	private String alertDestinationInfo;
	private LocalDateTime createdDate;

	@Builder
	private AlertHistoryResponse(Long id, String alertDescription, AlertHistoryStatus alertHistoryStatus,
		LocalDateTime createdDate, AlertDestinationType alertDestinationType, String alertDestinationInfo) {
		this.id = id;
		this.alertDescription = alertDescription;
		this.alertHistoryStatus = alertHistoryStatus;
		this.alertDestinationType = alertDestinationType;
		this.alertDestinationInfo = alertDestinationInfo;
		this.createdDate = createdDate;
	}

	public static AlertHistoryResponse of(AlertHistory alertHistory) {
		return AlertHistoryResponse.builder()
			.id(alertHistory.getId())
			.alertDescription(alertHistory.getAlertDescription())
			.alertHistoryStatus(alertHistory.getAlertHistoryStatus())
			.alertDestinationType(alertHistory.getAlertDestinationType())
			.alertDestinationInfo(alertHistory.getAlertDestinationInfo())
			.createdDate(alertHistory.getCreatedDate())
			.build();
	}
}
