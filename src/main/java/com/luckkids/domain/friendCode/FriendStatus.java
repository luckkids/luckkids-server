package com.luckkids.domain.friendCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendStatus {
    ME("내가 보낸 초대예요"),
    ALREADY("이미 {nickname}님과 친구예요"),
    FRIEND("{nickname}님이 친구초대를 보냈어요");

    private final String text;
}
