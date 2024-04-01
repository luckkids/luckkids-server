package com.luckkids.api.controller.login.request;

import com.luckkids.api.service.login.request.LoginServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "이메일은 필수입니다.")
    private String email;
    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;
    @NotNull(message = "디바이스ID는 필수입니다.")
    private String deviceId;
    private String pushKey;

    @Builder
    private LoginRequest(String email, String password, String deviceId, String pushKey) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
        this.pushKey = pushKey;
    }

    public LoginServiceRequest toServiceRequest(){
        return LoginServiceRequest.builder()
            .email(email)
            .password(password)
            .deviceId(deviceId)
            .pushKey(pushKey)
            .build();
    }
}
