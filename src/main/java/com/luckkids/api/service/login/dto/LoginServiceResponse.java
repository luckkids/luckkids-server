package com.luckkids.api.service.login.dto;

import com.luckkids.api.controller.login.dto.LoginResponse;
import com.luckkids.domain.user.User;
import com.luckkids.security.dto.JwtToken;
import lombok.Getter;

@Getter
public class LoginServiceResponse {
    private String email;
    private String profile;
    private String accessToken;
    private String refreshToken;

    public LoginServiceResponse(User user, JwtToken jwtToken) {
        email = user.getEmail();
        profile = user.getProfile();
        accessToken = jwtToken.getAccessToken();
        refreshToken = jwtToken.getRefreshToken();
    }

    public LoginResponse toRequest(){
        return LoginResponse.builder()
                .email(email)
                .profile(profile)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
