package com.luckkids.api.controller.alertSetting.request;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.luckkids.api.service.alertSetting.request.AlertSettingLuckMessageAlertTimeServiceRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertSettingLuckMessageAlertTimeRequest {

	@NotNull(message = "알림 시간은 필수입니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime luckMessageAlertTime;
	@NotBlank(message = "디바이스ID는 필수입니다.")
	private String deviceId;

	@Builder
	private AlertSettingLuckMessageAlertTimeRequest(LocalTime luckMessageAlertTime, String deviceId) {
		this.luckMessageAlertTime = luckMessageAlertTime;
		this.deviceId = deviceId;
	}

	public AlertSettingLuckMessageAlertTimeServiceRequest toServiceRequest() {
		return AlertSettingLuckMessageAlertTimeServiceRequest.builder()
			.luckMessageAlertTime(luckMessageAlertTime)
			.deviceId(deviceId)
			.build();
	}
}
