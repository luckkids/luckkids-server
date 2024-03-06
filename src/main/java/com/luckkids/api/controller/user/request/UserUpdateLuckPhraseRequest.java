package com.luckkids.api.controller.user.request;

import com.luckkids.api.service.user.request.UserUpdateLuckPhraseServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateLuckPhraseRequest {

    @NotBlank(message = "변경 할 행운문구는 필수입니다.")
    private String luckPhrase;

    @Builder
    private UserUpdateLuckPhraseRequest(String luckPhrase) {
        this.luckPhrase = luckPhrase;
    }

    public UserUpdateLuckPhraseServiceRequest toServiceRequest() {
        return UserUpdateLuckPhraseServiceRequest.builder()
            .luckPhrase(luckPhrase)
            .build();
    }

}
