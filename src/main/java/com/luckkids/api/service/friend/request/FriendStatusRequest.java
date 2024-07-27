package com.luckkids.api.service.friend.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendStatusRequest {
    private int requestId;
    private int receiverId;

    @Builder
    public FriendStatusRequest(int requestId, int receiverId) {
        this.requestId = requestId;
        this.receiverId = receiverId;
    }

    public static FriendStatusRequest of(int requestId, int receiverId) {
        return FriendStatusRequest.builder()
                .requestId(requestId)
                .receiverId(receiverId)
                .build();
    }
}
