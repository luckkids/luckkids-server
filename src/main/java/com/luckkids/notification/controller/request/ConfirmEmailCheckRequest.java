package com.luckkids.notification.controller.request;

import com.luckkids.notification.service.request.ConfirmEmailCheckServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfirmEmailCheckRequest {

    @NotNull(message = "이메일은 필수입니다.")
    private String email;
    @NotNull(message = "인증키는 필수입니다.")
    private String authKey;

    @Builder
    private ConfirmEmailCheckRequest(String email, String authKey) {
        this.email = email;
        this.authKey = authKey;
    }

    public ConfirmEmailCheckServiceRequest toServiceRequest(){
        return ConfirmEmailCheckServiceRequest.builder()
            .email(email)
            .authKey(authKey)
            .build();
    }
}
