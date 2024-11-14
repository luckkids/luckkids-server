package com.luckkids.api.service.friend.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendDeleteResponse {

    private int friendId;

    @Builder
    public FriendDeleteResponse(int friendId) {
        this.friendId = friendId;
    }

    public static FriendDeleteResponse of(int friendId) {
        return FriendDeleteResponse.builder().friendId(friendId).build();
    }
}
