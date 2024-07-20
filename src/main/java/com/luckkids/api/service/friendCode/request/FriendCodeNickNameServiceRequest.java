package com.luckkids.api.service.friendCode.request;

import com.luckkids.api.service.friend.request.FriendStatusRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendCodeNickNameServiceRequest {
    private String code;

    @Builder
    private FriendCodeNickNameServiceRequest(String code) {
        this.code = code;
    }

}