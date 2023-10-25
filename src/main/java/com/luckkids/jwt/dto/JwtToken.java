package com.luckkids.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long expiresIn;

    @Builder
    private JwtToken(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.grantType = grantType;
        this.expiresIn = expiresIn;
    }

    public static JwtToken of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        return JwtToken.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .grantType(grantType)
            .expiresIn(expiresIn)
            .build();
    }
}
