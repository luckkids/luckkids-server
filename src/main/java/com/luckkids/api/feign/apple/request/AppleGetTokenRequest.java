package com.luckkids.api.feign.apple.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppleGetTokenRequest {

    private String client_id;
    private String client_secret;
    private String code;
    private String grant_type;

    @Builder
    private AppleGetTokenRequest(String client_id, String client_secret, String code, String grant_type) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.code = code;
        this.grant_type = grant_type;
    }
}
