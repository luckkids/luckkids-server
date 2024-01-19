package com.luckkids.api.service.friendCode.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendInviteCodeResponse {
    private String code;

    @Builder
    private FriendInviteCodeResponse(String code) {
        this.code = code;
    }

    public static FriendInviteCodeResponse of(String code){
        return FriendInviteCodeResponse.builder()
            .code(code)
            .build();
    }
}
