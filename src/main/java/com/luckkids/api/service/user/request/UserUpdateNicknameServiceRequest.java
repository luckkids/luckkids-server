package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateNicknameServiceRequest {

    private String nickname;

    @Builder
    private UserUpdateNicknameServiceRequest(String nickname) {
        this.nickname = nickname;
    }
}
