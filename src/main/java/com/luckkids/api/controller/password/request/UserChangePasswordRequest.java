package com.luckkids.api.controller.password.request;

import com.luckkids.api.service.user.request.UserChangePasswordServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    @NotBlank(message = "패스워드는 필수입니다.")
    private String password;

    @Builder
    private UserChangePasswordRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserChangePasswordServiceRequest toServiceRequest(){
        return UserChangePasswordServiceRequest.builder()
            .email(email)
            .password(password)
            .build();
    }

}
