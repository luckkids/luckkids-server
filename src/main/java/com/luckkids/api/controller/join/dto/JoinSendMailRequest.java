package com.luckkids.api.controller.join.dto;

import com.luckkids.api.service.join.dto.JoinSendMailServiceRequest;
import lombok.Getter;

@Getter
public class JoinSendMailRequest {

    private String email;

    public JoinSendMailServiceRequest toServiceRequest(){
        return JoinSendMailServiceRequest.builder()
                .email(email)
                .build();
    }

}
