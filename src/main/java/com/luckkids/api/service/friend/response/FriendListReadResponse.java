package com.luckkids.api.service.friend.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class FriendListReadResponse {
    private int friendId;
    private String characterName;
    private String fileUrl;
    private int missionCount;

    @Builder
    private FriendListReadResponse(int friendId, String characterName, String fileUrl, int missionCount) {
        this.friendId = friendId;
        this.characterName = characterName;
        this.fileUrl = fileUrl;
        this.missionCount = missionCount;
    }
}
