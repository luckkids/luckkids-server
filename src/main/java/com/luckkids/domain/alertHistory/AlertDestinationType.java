package com.luckkids.domain.alertHistory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertDestinationType {

	FRIEND("친구 추가 알림"),
	MISSION("습관 추가 알림"),
	WEBVIEW("URL이 필요한 알림");

	private final String text;
}
