package com.luckkids.domain.fortuneTestHistory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FortuneTestResultType {
	TOKKINGI("토끼이"),
	TAEYANGI("태양이"),
	NABY("네이피"),
	TURKEYI("터키이"),
	GURUMI("구르미");

	private final String description;
}
