package com.luckkids.api.controller.mail.request;

import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMailRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    public SendMailServiceRequest toServiceRequest(){
        return SendMailServiceRequest.builder()
                .email(email)
                .build();
    }

    @Builder
    public SendMailRequest(String email) {
        this.email = email;
    }
}
