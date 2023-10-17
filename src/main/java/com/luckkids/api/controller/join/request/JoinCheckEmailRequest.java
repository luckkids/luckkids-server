package com.luckkids.api.controller.join.request;

import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinCheckEmailRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    public JoinCheckEmailServiceRequest toServiceRequest(){
        return JoinCheckEmailServiceRequest.builder()
                .email(email)
                .build();
    }

    @Builder
    public JoinCheckEmailRequest(String email) {
        this.email = email;
    }
}
