package com.luckkids.api.service.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginServiceRequest {
    private String email;
    private String password;
    private String deviceId;
}
