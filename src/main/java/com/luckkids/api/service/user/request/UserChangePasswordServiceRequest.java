package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserChangePasswordServiceRequest {
    private String email;
    private String password;

    @Builder
    private UserChangePasswordServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
