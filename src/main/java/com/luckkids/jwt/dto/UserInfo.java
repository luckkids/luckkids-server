package com.luckkids.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    private int userId;
    private String email;

    @Builder
    private UserInfo(int userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public static UserInfo of(int userId, String email){
        return UserInfo.builder()
            .userId(userId)
            .email(email)
            .build();
    }
}
