package com.luckkids.notification.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingServiceRequest {

	private String deviceId;

	@Builder
	private AlertSettingServiceRequest(String deviceId) {
		this.deviceId = deviceId;
	}

}
