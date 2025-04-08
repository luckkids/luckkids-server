package com.luckkids.api.controller.luckMessageHistory.request;

import com.luckkids.api.service.luckMessageHistory.request.LuckMessageHistoryServiceRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LuckMessageHistoryRequest {

	@NotBlank(message = "디바이스ID는 필수입니다.")
	private String deviceId;

	@Builder
	private LuckMessageHistoryRequest(String deviceId) {
		this.deviceId = deviceId;
	}

	public LuckMessageHistoryServiceRequest toServiceRequest() {
		return LuckMessageHistoryServiceRequest.builder()
			.deviceId(deviceId)
			.build();
	}
}
