package com.luckkids.api.controller.join.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinResponse {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String snsType;
}
