package com.luckkids.api.service.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLeagueResponse {
    private String nickname;
    private String imageFileUrl;
    private int characterCount;

    @Builder
    private UserLeagueResponse(String nickname, String imageFileUrl, int characterCount) {
        this.nickname = nickname;
        this.imageFileUrl = imageFileUrl;
        this.characterCount = characterCount;
    }
}
