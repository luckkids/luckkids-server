package com.luckkids.api.controller.alertHistory.request;

import com.luckkids.api.page.request.PageInfoRequest;
import com.luckkids.api.service.alertHistory.request.AlertHistoryDeviceIdServiceRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AlertHistoryDeviceIdRequest extends PageInfoRequest {

	@NotBlank(message = "디바이스 ID는 필수입니다.")
	private String deviceId;

	public AlertHistoryDeviceIdServiceRequest toServiceRequest() {
		return AlertHistoryDeviceIdServiceRequest.builder()
			.page(getPage())
			.size(getSize())
			.deviceId(deviceId)
			.build();
	}
}
