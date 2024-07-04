package com.luckkids.api.service.alertHistory.request;

import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.push.Push;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertHistoryServiceRequest {

	private Push push;
	private String alertDescription;

	@Builder
	private AlertHistoryServiceRequest(Push push, String alertDescription) {
		this.push = push;
		this.alertDescription = alertDescription;
	}

	public AlertHistory toEntity() {
		return AlertHistory.builder()
			// .push(push)  // ⭐️⭐️ 수정해야함 !
			.alertDescription(alertDescription)
			.alertHistoryStatus(AlertHistoryStatus.UNCHECKED)
			.build();
	}

	public static AlertHistoryServiceRequest of(Push push, String alertDescription) {
		return AlertHistoryServiceRequest.builder()
			.push(push)
			.alertDescription(alertDescription)
			.build();
	}
}
