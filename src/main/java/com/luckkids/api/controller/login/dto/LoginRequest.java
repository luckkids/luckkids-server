package com.luckkids.api.controller.login.dto;

import com.luckkids.api.service.login.dto.LoginServiceRequest;
import lombok.Getter;

@Getter
public class LoginRequest {

    private String email;
    private String password;
    private String deviceId;

    public LoginServiceRequest toServiceRequest(){
        return LoginServiceRequest.builder()
                .email(email)
                .password(password)
                .deviceId(deviceId)
                .build();
    }
}
