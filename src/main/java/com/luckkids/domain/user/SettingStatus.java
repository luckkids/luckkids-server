package com.luckkids.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettingStatus {
    NOCOMPLETE("미완료"),
    COMPLETE("완료");

    private final String text;
}
