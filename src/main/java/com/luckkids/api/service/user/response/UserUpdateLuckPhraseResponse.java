package com.luckkids.api.service.user.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateLuckPhraseResponse {
    private String luckPhrase;

    @Builder
    private UserUpdateLuckPhraseResponse(String luckPhrase) {
        this.luckPhrase = luckPhrase;
    }

    public static UserUpdateLuckPhraseResponse of(User user) {
        return UserUpdateLuckPhraseResponse.builder()
            .luckPhrase(user.getLuckPhrase())
            .build();
    }
}