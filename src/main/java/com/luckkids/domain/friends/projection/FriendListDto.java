package com.luckkids.domain.friends.projection;

import com.luckkids.api.service.friend.response.FriendListReadResponse;

public record FriendListDto(
    int friendId,
    String characterName,
    String fileUrl,
    int missionCount) {

    public FriendListReadResponse toServiceResponse() {
        return FriendListReadResponse.builder()
            .friendId(friendId)
            .characterName(characterName)
            .fileUrl(fileUrl)
            .missionCount(missionCount)
            .build();
    }
}
