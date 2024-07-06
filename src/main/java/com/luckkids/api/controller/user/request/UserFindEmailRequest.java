package com.luckkids.api.controller.user.request;

import com.luckkids.api.service.user.request.UserFindEmailServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserFindEmailRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Builder
    private UserFindEmailRequest(String email) {
        this.email = email;
    }

    public UserFindEmailServiceRequest toServiceRequest(){
        return UserFindEmailServiceRequest.builder()
            .email(email)
            .build();
    }
}
