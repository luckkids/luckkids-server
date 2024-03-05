package com.luckkids.api.service.alertHistory.request;

import com.luckkids.api.page.request.PageInfoServiceRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class AlertHistoryDeviceIdServiceRequest extends PageInfoServiceRequest {

	private String deviceId;

}

