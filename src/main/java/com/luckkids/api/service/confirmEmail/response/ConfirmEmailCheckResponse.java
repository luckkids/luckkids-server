package com.luckkids.api.service.confirmEmail.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConfirmEmailCheckResponse {

    private String email;

    @Builder
    private ConfirmEmailCheckResponse(String email) {
        this.email = email;
    }

    public static ConfirmEmailCheckResponse of(String email){
        return ConfirmEmailCheckResponse.builder()
            .email(email)
            .build();
    }
}
