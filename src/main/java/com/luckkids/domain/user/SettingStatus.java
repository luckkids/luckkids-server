package com.luckkids.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettingStatus {
    NO("미완료"),
    YES("완료");

    private final String text;
}
