package com.luckkids.security.dto;

import com.luckkids.domain.refreshToken.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long expiresIn;

    public static JwtToken of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        return new JwtToken(accessToken, refreshToken, grantType, expiresIn);
    }

    public void checkRefreshToken(List<RefreshToken> refreshTokens){

    }
}
