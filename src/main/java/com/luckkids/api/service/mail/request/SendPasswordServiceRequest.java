package com.luckkids.api.service.mail.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SendPasswordServiceRequest {

    private String email;

    @Builder
    private SendPasswordServiceRequest(String email) {
        this.email = email;
    }
}
