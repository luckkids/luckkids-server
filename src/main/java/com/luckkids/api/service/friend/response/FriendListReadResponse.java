package com.luckkids.api.service.friend.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendListReadResponse {
    private int friendId;
    private String characterName;
    private String fileUrl;
    private int cloverCount;

    @Builder
    private FriendListReadResponse(int friendId, String characterName, String fileUrl, int cloverCount) {
        this.friendId = friendId;
        this.characterName = characterName;
        this.fileUrl = fileUrl;
        this.cloverCount = cloverCount;
    }
}
