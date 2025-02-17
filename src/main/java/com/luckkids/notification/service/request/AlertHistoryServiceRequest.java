package com.luckkids.notification.service.request;

import com.luckkids.api.service.firebase.request.SendFirebaseServiceRequest;
import com.luckkids.notification.domain.alertHistory.AlertDestinationType;
import com.luckkids.notification.domain.alertHistory.AlertHistory;
import com.luckkids.notification.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertHistoryServiceRequest {

	private User user;
	private String alertDescription;
	private AlertDestinationType alertDestinationType;
	private String alertDestinationInfo;

	@Builder
	private AlertHistoryServiceRequest(User user, String alertDescription, AlertDestinationType alertDestinationType,
		String alertDestinationInfo) {
		this.user = user;
		this.alertDescription = alertDescription;
		this.alertDestinationType = alertDestinationType;
		this.alertDestinationInfo = alertDestinationInfo;
	}

	public AlertHistory toEntity() {
		return AlertHistory.builder()
			.user(user)
			.alertDescription(alertDescription)
			.alertHistoryStatus(AlertHistoryStatus.UNCHECKED)
			.alertDestinationType(alertDestinationType)
			.alertDestinationInfo(alertDestinationInfo)
			.build();
	}

	public static AlertHistoryServiceRequest of(SendFirebaseServiceRequest sendPushServiceRequest) {
		return AlertHistoryServiceRequest.builder()
			.user(sendPushServiceRequest.getPush().getUser())
			.alertDescription(sendPushServiceRequest.getBody())
			.alertDestinationType(sendPushServiceRequest.getSendFirebaseDataDto().getAlert_destination_type())
			.alertDestinationInfo(sendPushServiceRequest.getSendFirebaseDataDto().getAlert_destination_info())
			.build();
	}
}
