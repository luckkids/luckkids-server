package com.luckkids.api.service.version.request;

import com.luckkids.notification.service.request.SendPushAlertTypeServiceRequest;
import com.luckkids.notification.service.request.SendPushDataDto;
import com.luckkids.notification.domain.alertHistory.AlertDestinationType;
import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.notification.domain.push.PushMessage;
import com.luckkids.domain.version.Version;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VersionSaveServiceRequest {
	private String versionNum;
	private String url;

	@Builder
	private VersionSaveServiceRequest(String versionNum, String url) {
		this.versionNum = versionNum;
		this.url = url;
	}

	public Version toEntity() {
		return Version.builder()
			.versionNum(versionNum)
			.build();
	}

	public SendPushAlertTypeServiceRequest toSendPushAlertTypeRequest(AlertType alertType) {
		return SendPushAlertTypeServiceRequest.builder()
			.alertType(alertType)
			.body(PushMessage.APP_UPDATE.getText())
			.sendPushDataDto(
				SendPushDataDto.builder()
					.alert_destination_type(AlertDestinationType.WEBVIEW)
					.alert_destination_info(url)
					.build())
			.build();
	}
}
