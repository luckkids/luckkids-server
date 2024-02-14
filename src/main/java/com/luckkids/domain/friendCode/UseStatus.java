package com.luckkids.domain.friendCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UseStatus {
    USED("사용"),
    UNUSED("미사용");

    private final String text;
}
