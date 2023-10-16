package com.luckkids.api.controller.join.dto;

import com.luckkids.api.service.join.dto.JoinServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
