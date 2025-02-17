package com.luckkids.notification.service.request;

import com.luckkids.api.service.firebase.request.SendFirebaseServiceRequest;
import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.notification.domain.push.Push;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendPushAlertTypeServiceRequest {

	private AlertType alertType;
	private SendPushDataDto sendPushDataDto;
	private String body;

	@Builder
	private SendPushAlertTypeServiceRequest(AlertType alertType, SendPushDataDto sendPushDataDto, String body) {
		this.alertType = alertType;
		this.sendPushDataDto = sendPushDataDto;
		this.body = body;
	}

	public SendFirebaseServiceRequest toSendPushServiceRequest(Push push) {
		return SendFirebaseServiceRequest.builder()
			.body(body)
			.push(push)
			.sendFirebaseDataDto(sendPushDataDto.toFirebaseDataDto())
			.build();
	}

}
