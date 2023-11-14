package com.luckkids.domain.alertSetting;

import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.user.SnsType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum AlertType {

    ENTIRE("전체"),
    MISSION("미션"),
    LUCK("행운"),
    NOTICE("공지사항");

    private final String text;
}
