package com.luckkids.domain.missionOutcome;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionStatus {

    SUCCEED("성공"),
    FAILED("실패");

    private final String text;
}
