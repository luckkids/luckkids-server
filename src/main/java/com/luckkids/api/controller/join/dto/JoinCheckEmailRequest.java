package com.luckkids.api.controller.join.dto;

import com.luckkids.api.service.join.dto.JoinCheckEmailServiceRequest;
import lombok.Getter;

@Getter
public class JoinCheckEmailRequest {

    private String email;

    public JoinCheckEmailServiceRequest toServiceRequest(){
        return JoinCheckEmailServiceRequest.builder()
                .email(email)
                .build();
    }
}
