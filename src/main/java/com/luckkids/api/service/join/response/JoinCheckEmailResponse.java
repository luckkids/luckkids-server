package com.luckkids.api.service.join.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinCheckEmailResponse {
    private String email;

    public static JoinCheckEmailResponse of(String email){
        return JoinCheckEmailResponse.builder()
            .email(email)
            .build();
    }

    @Builder
    private JoinCheckEmailResponse(String email) {
        this.email = email;
    }
}
