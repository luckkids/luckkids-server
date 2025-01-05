package com.luckkids.api.service.alertHistory.request;

import com.luckkids.api.service.firebase.request.SendFirebaseServiceRequest;
import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.domain.alertHistory.AlertDestinationType;
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
	private AlertDestinationType alertDestinationType;
	private String alertDestinationInfo;

	@Builder
	private AlertHistoryServiceRequest(User user, String alertDescription, AlertDestinationType alertDestinationType, String alertDestinationInfo) {
		this.user = user;
		this.alertDescription = alertDescription;
		this.alertDestinationType = alertDestinationType;
		this.alertDestinationInfo = alertDestinationInfo;
	}

	public AlertHistory toEntity(AlertHistoryStatus alertHistoryStatus) {
		return AlertHistory.builder()
			.user(user)
			.alertDescription(alertDescription)
			.alertHistoryStatus(alertHistoryStatus)
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
