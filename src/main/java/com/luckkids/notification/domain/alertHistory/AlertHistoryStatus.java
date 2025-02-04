package com.luckkids.notification.domain.alertHistory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertHistoryStatus {

	CHECKED("확인"),
	UNCHECKED("미확인");

	private final String text;
}
