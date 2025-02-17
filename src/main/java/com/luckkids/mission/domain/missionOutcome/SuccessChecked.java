package com.luckkids.mission.domain.missionOutcome;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessChecked {

    FIRST_CHECKED("확인"),
    UNCHECKED("미확인");

    private final String text;
}
