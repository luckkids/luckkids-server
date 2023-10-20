package com.luckkids.api.service.login.response;

import com.luckkids.domain.user.User;
import com.luckkids.jwt.dto.JwtToken;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String email;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(User user, JwtToken jwtToken){
        return LoginResponse.builder()
            .email(user.getEmail())
            .accessToken(jwtToken.getAccessToken())
            .refreshToken(jwtToken.getRefreshToken())
            .build();
    }
}
