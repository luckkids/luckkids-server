package com.luckkids.api.service.confirmEmail.request;

import com.luckkids.domain.confirmEmail.ConfirmEmail;
import com.luckkids.domain.confirmEmail.ConfirmStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateConfrimEmailServiceRequest {
    private String email;
    private String authKey;

    @Builder
    private CreateConfrimEmailServiceRequest(String email, String authKey) {
        this.email = email;
        this.authKey = authKey;
    }

    public ConfirmEmail toEntity(){
        return ConfirmEmail.builder()
            .email(email)
            .authKey(authKey)
            .confirmStatus(ConfirmStatus.INCOMPLETE)
            .build();
    }
}
