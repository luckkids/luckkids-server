package com.luckkids.api.service.user.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePasswordResponse {
    private String email;

    @Builder
    private UserUpdatePasswordResponse(String email) {
        this.email = email;
    }

    public static UserUpdatePasswordResponse of(User user){
        return UserUpdatePasswordResponse.builder()
            .email(user.getEmail())
            .build();
    }
}
