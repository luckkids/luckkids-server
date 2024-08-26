package com.luckkids.domain.alertSetting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertType {

    ENTIRE("전체"),
    MISSION("미션"),
    LUCK("행운"),
    FRIEND("친구"),
    NOTICE("공지사항");

    private final String text;
}
