package com.luckkids.domain.misson;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionType {

    HOUSEKEEPING("집 정돈"),
    SELF_CARE("셀프케어"),
    HEALTH("건강"),
    WORK("일"),
    MINDSET("마인드셋"),
    SELF_DEVELOPMENT("자기계발");

    private final String text;
}
