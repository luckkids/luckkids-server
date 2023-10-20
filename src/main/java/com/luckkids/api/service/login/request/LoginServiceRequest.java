package com.luckkids.api.service.login.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginServiceRequest {
    private String email;
    private String password;
    private String deviceId;
    private String pushKey;

    @Builder
    public LoginServiceRequest(String email, String password, String deviceId, String pushKey) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
        this.pushKey = pushKey;
    }
}
