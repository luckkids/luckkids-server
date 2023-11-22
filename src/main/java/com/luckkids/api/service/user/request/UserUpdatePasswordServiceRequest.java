package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePasswordServiceRequest {
    private String email;
    private String password;

    @Builder
    private UserUpdatePasswordServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
