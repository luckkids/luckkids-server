package com.luckkids.domain.friend.projection;

import com.luckkids.domain.userCharacter.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendProfileDto {
    private int friendId;
    private String nickname;
    private String luckPhrase;
    private String imageFileUrl;
    private int characterCount;

    public FriendProfileDto(int friendId, String nickname, String luckPhrase, String imageFileUrl, int missionCount) {
        this.friendId = friendId;
        this.nickname = nickname;
        this.luckPhrase = luckPhrase;
        this.imageFileUrl = imageFileUrl;
        this.characterCount = missionCount / Level.LEVEL_MAX.getScoreThreshold();
    }
}