package com.luckkids.domain.push;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum PushScreenName {

    WEBVIEW("WebView"),
    MISSION_REPAIR("MissionRepair"),
    GARDEN("garden");

    private final String text;

}
