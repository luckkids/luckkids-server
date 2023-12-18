package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLuckPhrasesServiceRequest {

    private String luckPhrases;

    @Builder
    private UserLuckPhrasesServiceRequest(String luckPhrases) {
        this.luckPhrases = luckPhrases;
    }
}
