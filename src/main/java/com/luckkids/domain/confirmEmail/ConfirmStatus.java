package com.luckkids.domain.confirmEmail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfirmStatus {
    INCOMPLETE("미완료"),
    COMPLETE("완료");

    private final String text;
}
