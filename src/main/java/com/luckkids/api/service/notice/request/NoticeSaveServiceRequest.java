package com.luckkids.api.service.notice.request;

import com.luckkids.notification.service.request.SendPushAlertTypeServiceRequest;
import com.luckkids.notification.service.request.SendPushDataDto;
import com.luckkids.notification.domain.alertHistory.AlertDestinationType;
import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.domain.notice.Notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveServiceRequest {

	private String title;
	private String url;

	@Builder
	private NoticeSaveServiceRequest(String title, String url) {
		this.title = title;
		this.url = url;
	}

	public Notice toEntity() {
		return Notice.builder()
			.title(title)
			.url(url)
			.build();
	}

	public SendPushAlertTypeServiceRequest toSendPushAlertTypeRequest(AlertType alertType) {
		return SendPushAlertTypeServiceRequest.builder()
			.alertType(alertType)
			.body(title)
			.sendPushDataDto(
				SendPushDataDto.builder()
					.alert_destination_type(AlertDestinationType.WEBVIEW)
					.alert_destination_info(url)
					.build()
			)
			.build();
	}
}
