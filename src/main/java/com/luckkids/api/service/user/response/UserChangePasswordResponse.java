package com.luckkids.api.service.user.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserChangePasswordResponse {
    private String email;

    @Builder
    private UserChangePasswordResponse(String email) {
        this.email = email;
    }

    public static UserChangePasswordResponse of(User user){
        return UserChangePasswordResponse.builder()
            .email(user.getEmail())
            .build();
    }
}
