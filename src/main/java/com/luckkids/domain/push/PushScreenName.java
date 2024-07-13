package com.luckkids.domain.push;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum PushScreenName {

    WEBVIEW("WebView"),
    MISSION("mission"),
    FRIEND("friend");

    private final String text;

}
