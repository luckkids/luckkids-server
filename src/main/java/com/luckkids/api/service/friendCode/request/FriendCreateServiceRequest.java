package com.luckkids.api.service.friendCode.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class FriendCreateServiceRequest {
    private String code;

    @Builder
    private FriendCreateServiceRequest(String code) {
        this.code = code;
    }
}
