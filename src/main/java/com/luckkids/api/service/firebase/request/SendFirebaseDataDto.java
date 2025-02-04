package com.luckkids.api.service.firebase.request;

import com.luckkids.notification.domain.alertHistory.AlertDestinationType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendFirebaseDataDto {

	private AlertDestinationType alert_destination_type;
	private String alert_destination_info;

	@Builder
	private SendFirebaseDataDto(AlertDestinationType alert_destination_type, String alert_destination_info) {
		this.alert_destination_type = alert_destination_type;
		this.alert_destination_info = alert_destination_info;
	}
}
