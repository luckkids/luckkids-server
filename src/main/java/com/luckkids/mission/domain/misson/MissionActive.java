package com.luckkids.mission.domain.misson;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionActive {

	TRUE("활성화"),
	FALSE("비활성화");

	private final String text;
}
