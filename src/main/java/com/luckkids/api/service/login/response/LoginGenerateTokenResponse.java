package com.luckkids.api.service.login.response;

import com.luckkids.jwt.dto.JwtToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginGenerateTokenResponse {
    private String accessToken;
    private String refreshToken;

    @Builder
    private LoginGenerateTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginGenerateTokenResponse of(JwtToken jwtToken){
        return LoginGenerateTokenResponse.builder()
            .accessToken(jwtToken.getAccessToken())
            .refreshToken(jwtToken.getRefreshToken())
            .build();
    }
}
