package com.luckkids.api.service.user.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLuckPhrasesResponse {
    private String luckPhrases;

    @Builder
    private UserLuckPhrasesResponse(String luckPhrases) {
        this.luckPhrases = luckPhrases;
    }

    public static UserLuckPhrasesResponse of(User user){
        return UserLuckPhrasesResponse.builder()
            .luckPhrases(user.getLuckPhrases())
            .build();
    }
}