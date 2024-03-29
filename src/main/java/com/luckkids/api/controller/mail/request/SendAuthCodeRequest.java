package com.luckkids.api.controller.mail.request;

import com.luckkids.api.service.mail.request.SendAuthCodeServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendAuthCodeRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    public SendAuthCodeServiceRequest toServiceRequest(){
        return SendAuthCodeServiceRequest.builder()
                .email(email)
                .build();
    }

    @Builder
    public SendAuthCodeRequest(String email) {
        this.email = email;
    }
}
