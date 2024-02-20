package com.luckkids.domain.user.projection;

import com.luckkids.api.service.user.response.UserLeagueResponse;
import com.luckkids.domain.userCharacter.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLeagueDto {
    private String nickname;
    private String imageFileUrl;
    private int characterCount;

    public UserLeagueDto(String nickname, String imageFileUrl, int missionCount) {
        this.nickname = nickname;
        this.imageFileUrl = imageFileUrl;
        this.characterCount = missionCount / Level.LEVEL_MAX.getScoreThreshold();
    }

    public UserLeagueResponse toUserLeagueResponse() {
        return UserLeagueResponse.builder()
            .nickname(nickname)
            .imageFileUrl(imageFileUrl)
            .characterCount(characterCount)
            .build();
    }
}
