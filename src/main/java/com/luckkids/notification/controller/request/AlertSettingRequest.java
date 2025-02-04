package com.luckkids.notification.controller.request;

import com.luckkids.notification.service.request.AlertSettingServiceRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlertSettingRequest {

	@NotBlank(message = "디바이스ID는 필수입니다.")
	private String deviceId;

	@Builder
	private AlertSettingRequest(String deviceId) {
		this.deviceId = deviceId;
	}

	public AlertSettingServiceRequest toServiceRequest() {
		return AlertSettingServiceRequest.builder()
			.deviceId(deviceId)
			.build();
	}

}
