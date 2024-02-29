package com.luckkids.api.service.user.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateNicknameResponse {
    private String nickname;

    @Builder
    private UserUpdateNicknameResponse(String nickname) {
        this.nickname = nickname;
    }

    public static UserUpdateNicknameResponse of(String nickname) {
        return UserUpdateNicknameResponse.builder()
            .nickname(nickname)
            .build();
    }
}