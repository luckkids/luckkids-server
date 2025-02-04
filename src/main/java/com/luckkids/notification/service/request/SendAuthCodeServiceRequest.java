package com.luckkids.notification.service.request;

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
