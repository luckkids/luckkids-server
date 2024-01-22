package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLuckPhraseServiceRequest {

    private String luckPhrase;

    @Builder
    private UserLuckPhraseServiceRequest(String luckPhrase) {
        this.luckPhrase = luckPhrase;
    }
}
