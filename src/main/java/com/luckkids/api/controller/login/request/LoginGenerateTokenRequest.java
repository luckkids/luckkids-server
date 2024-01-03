package com.luckkids.api.controller.login.request;

import com.luckkids.api.service.login.request.LoginGenerateTokenServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginGenerateTokenRequest {
    @NotNull(message = "리플래시토큰은 필수입니다.")
    private String refreshToken;
    @NotNull(message = "이메일은 필수입니다.")
    private String email;
    @NotNull(message = "디바이스ID는 필수입니다.")
    private String deviceId;

    @Builder
    private LoginGenerateTokenRequest(String refreshToken, String email, String deviceId) {
        this.refreshToken = refreshToken;
        this.email = email;
        this.deviceId = deviceId;
    }

    public LoginGenerateTokenServiceRequest toServiceRequest(){
        return LoginGenerateTokenServiceRequest.builder()
            .refreshToken(refreshToken)
            .email(email)
            .deviceId(deviceId)
            .build();
    }
}
