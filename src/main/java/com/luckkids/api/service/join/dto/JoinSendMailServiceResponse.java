package com.luckkids.api.service.join.dto;

import com.luckkids.api.controller.join.dto.JoinSendMailResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinSendMailServiceResponse {

    private String code;

    public JoinSendMailResponse toControllerResponse(){
        return JoinSendMailResponse.builder()
                .code(code)
                .build();
    }
}
