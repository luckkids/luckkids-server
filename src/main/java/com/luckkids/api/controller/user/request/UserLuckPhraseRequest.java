package com.luckkids.api.controller.user.request;

import com.luckkids.api.service.user.request.UserLuckPhraseServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLuckPhraseRequest {

    @NotBlank(message = "변경 할 행운문구는 필수입니다.")
    private String luckPhrase;

    @Builder
    private UserLuckPhraseRequest(String luckPhrase) {
        this.luckPhrase = luckPhrase;
    }

    public UserLuckPhraseServiceRequest toServiceRequest() {
        return UserLuckPhraseServiceRequest.builder()
            .luckPhrase(luckPhrase)
            .build();
    }

}
