package com.luckkids.api.controller.join.dto;

import com.luckkids.api.service.join.dto.JoinServiceRequest;
import lombok.Getter;

@Getter
public class JoinRequest {

    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;

    public JoinServiceRequest toServiceRequest(){
        return JoinServiceRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .build();
    }
}
