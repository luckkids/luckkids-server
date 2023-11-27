package com.luckkids.api.controller.mail.request;

import com.luckkids.api.service.user.request.UserFindSnsTypeServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MailFindSnsTypeRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Builder
    private MailFindSnsTypeRequest(String email) {
        this.email = email;
    }

    public UserFindSnsTypeServiceRequest toServiceRequest(){
        return UserFindSnsTypeServiceRequest.builder()
            .email(email)
            .build();
    }
}
