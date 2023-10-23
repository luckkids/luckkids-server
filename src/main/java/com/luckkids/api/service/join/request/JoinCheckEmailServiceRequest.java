package com.luckkids.api.service.join.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinCheckEmailServiceRequest {

    private String email;

    @Builder
    private JoinCheckEmailServiceRequest(String email) {
        this.email = email;
    }
}
