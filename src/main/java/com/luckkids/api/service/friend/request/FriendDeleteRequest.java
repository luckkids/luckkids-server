package com.luckkids.api.service.friend.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendDeleteRequest {

    private int friendId;

    @Builder
    public FriendDeleteRequest(int friendId) {
        this.friendId = friendId;
    }
}
