package com.luckkids.api.service.luckMessageHistory.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LuckMessageHistoryServiceRequest {
	private String deviceId;

	@Builder
	private LuckMessageHistoryServiceRequest(String deviceId) {
		this.deviceId = deviceId;
	}
}
