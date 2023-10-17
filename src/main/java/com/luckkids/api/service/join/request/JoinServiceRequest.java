package com.luckkids.api.service.join.request;

import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinServiceRequest {

    private String email;
    private String password;
    private String phoneNumber;

    public User createUser(){
        return User.builder()
                .email(email)
                .password(encryptPassword())
                .phoneNumber(phoneNumber)
                .snsType(SnsType.NORMAL)
                .build();
    }

    public String encryptPassword(){
        return password;
    }
}
