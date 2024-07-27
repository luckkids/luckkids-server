package com.luckkids.api.service.friendCode.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendRefuseResponse {

    private String code;

    @Builder
    private FriendRefuseResponse(String code) {
        this.code = code;
    }

    public static FriendRefuseResponse of(String code){
        return FriendRefuseResponse.builder()
                .code(code)
                .build();
    }
}
