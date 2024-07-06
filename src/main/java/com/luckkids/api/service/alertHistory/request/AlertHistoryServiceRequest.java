package com.luckkids.api.service.alertHistory.request;

import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertHistoryServiceRequest {

	private User user;
	private String alertDescription;

	@Builder
	private AlertHistoryServiceRequest(User user, String alertDescription) {
		this.user = user;
		this.alertDescription = alertDescription;
	}

	public AlertHistory toEntity() {
		return AlertHistory.builder()
			.user(user)
			.alertDescription(alertDescription)
			.alertHistoryStatus(AlertHistoryStatus.UNCHECKED)
			.build();
	}

	public static AlertHistoryServiceRequest of(User user, String alertDescription) {
		return AlertHistoryServiceRequest.builder()
			.user(user)
			.alertDescription(alertDescription)
			.build();
	}
}
