package com.luckkids.api.service.login.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginGenerateTokenServiceRequest {
    private String refreshToken;
    private String email;
    private String deviceId;

    @Builder
    private LoginGenerateTokenServiceRequest(String refreshToken, String email, String deviceId) {
        this.refreshToken = refreshToken;
        this.email = email;
        this.deviceId = deviceId;
    }
}
