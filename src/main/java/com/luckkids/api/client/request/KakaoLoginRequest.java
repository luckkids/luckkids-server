package com.luckkids.api.client.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest {

    private String authorization_code;

    @Builder
    private KakaoLoginRequest(String authorization_code) {
        this.authorization_code = authorization_code;
    }
}
