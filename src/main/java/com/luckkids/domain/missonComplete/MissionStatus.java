package com.luckkids.domain.missonComplete;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionStatus {

    SUCCEED("성공"),
    FAILED("실패"),
    AWAITING("대기");

    private final String text;
}
