package com.luckkids.api.service.user.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLuckPhraseResponse {
    private String luckPhrase;

    @Builder
    private UserLuckPhraseResponse(String luckPhrase) {
        this.luckPhrase = luckPhrase;
    }

    public static UserLuckPhraseResponse of(User user) {
        return UserLuckPhraseResponse.builder()
            .luckPhrase(user.getLuckPhrase())
            .build();
    }
}