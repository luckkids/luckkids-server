package com.luckkids.api.controller.mail.request;

import com.luckkids.api.service.mail.request.SendPasswordServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SendPasswordRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Builder
    private SendPasswordRequest(String email) {
        this.email = email;
    }

    public SendPasswordServiceRequest toServiceRequest(){
        return SendPasswordServiceRequest.builder()
            .email(email)
            .build();
    }
}
