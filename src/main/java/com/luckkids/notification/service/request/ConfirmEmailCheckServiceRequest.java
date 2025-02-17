package com.luckkids.notification.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfirmEmailCheckServiceRequest {
    private String email;
    private String authKey;

    @Builder
    private ConfirmEmailCheckServiceRequest(String email, String authKey) {
        this.email = email;
        this.authKey = authKey;
    }
}
