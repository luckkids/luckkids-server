package com.luckkids.mission.domain.missionOutcome;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionStatus {

    SUCCEED("성공", 1),
    FAILED("실패", -1);

    private final String text;
    private final int value;
}
