package com.luckkids.api.service.login.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest {
    private String code;

    @Builder
    private KakaoLoginRequest(String code) {
        this.code = code;
    }
}
