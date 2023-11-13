package com.luckkids.api.controller.join.request;

import com.luckkids.api.service.join.request.JoinServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    private JoinRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public JoinServiceRequest toServiceRequest(){
        return JoinServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
