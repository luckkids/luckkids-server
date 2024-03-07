package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateLuckPhraseServiceRequest {

    private String luckPhrase;

    @Builder
    private UserUpdateLuckPhraseServiceRequest(String luckPhrase) {
        this.luckPhrase = luckPhrase;
    }
}
