package com.luckkids.api.service.mail.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendMailServiceRequest {

    private String email;

    @Builder
    private SendMailServiceRequest(String email) {
        this.email = email;
    }
}
