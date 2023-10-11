package com.luckkids.domain.friends;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendStatus {

    REQUESTED("요청됨"),
    ACCEPTED("수락됨");

    private final String text;
}
