package com.luckkids.api.controller.join.request;

import com.luckkids.api.service.join.request.JoinSendMailServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinSendMailRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    public JoinSendMailServiceRequest toServiceRequest(){
        return JoinSendMailServiceRequest.builder()
                .email(email)
                .build();
    }

    @Builder
    public JoinSendMailRequest(String email) {
        this.email = email;
    }
}
