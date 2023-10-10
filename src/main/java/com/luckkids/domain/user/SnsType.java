package com.luckkids.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnsType {
    NORMAL("일반"),
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플");

    private final String text;
}
