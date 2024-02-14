package com.luckkids.domain.user.projection;

import com.luckkids.domain.userCharacter.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileDto {
    private int myId;
    private String nickname;
    private String luckPhrase;
    private String imageFileUrl;
    private int CharacterCount;

    public MyProfileDto(int myId, String nickname, String luckPhrase, String imageFileUrl, int missionCount) {
        this.myId = myId;
        this.nickname = nickname;
        this.luckPhrase = luckPhrase;
        this.imageFileUrl = imageFileUrl;
        this.CharacterCount = missionCount / Level.LEVEL_MAX.getScoreThreshold();
    }
}