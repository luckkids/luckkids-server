package com.luckkids.api.controller.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String email;
    private String profile;
    private String accessToken;
    private String refreshToken;
}
