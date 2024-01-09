package com.luckkids.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginUserInfo {
    private int userId;

    @Builder
    private LoginUserInfo(int userId) {
        this.userId = userId;
    }

    public static LoginUserInfo of(int userId) {
        return LoginUserInfo.builder()
            .userId(userId)
            .build();
    }
}
