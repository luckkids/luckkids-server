package com.luckkids.api.service.mail.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SendPasswordResponse {

    private String email;

    @Builder
    private SendPasswordResponse(String email) {
        this.email = email;
    }

    public static SendPasswordResponse of(String email){
        return SendPasswordResponse.builder()
            .email(email)
            .build();
    }
}
