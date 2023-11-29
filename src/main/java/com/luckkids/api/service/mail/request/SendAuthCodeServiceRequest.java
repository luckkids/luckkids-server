package com.luckkids.api.service.mail.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendAuthCodeServiceRequest {

    private String email;

    @Builder
    private SendAuthCodeServiceRequest(String email) {
        this.email = email;
    }
}
