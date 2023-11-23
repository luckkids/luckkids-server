package com.luckkids.api.service.mail.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SendPasswordResponse {

    private String tempPassword;

    @Builder
    private SendPasswordResponse(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public static SendPasswordResponse of(String tempPassword){
        return SendPasswordResponse.builder()
            .tempPassword(tempPassword)
            .build();
    }
}
