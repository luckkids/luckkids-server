package com.luckkids.domain.misson;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertStatus {

    CHECKED("확인"),
    UNCHECKED("미확인");

    private final String text;
}
