package com.luckkids.domain.friends.projection;

import com.luckkids.api.service.friend.response.FriendListReadResponse;

public record FriendListReadDto(
        int friendId,
        String characterName,
        String fileUrl,
        int cloverCount) {

    public FriendListReadResponse toServiceResponse(){
        return FriendListReadResponse.builder()
                .friendId(friendId)
                .characterName(characterName)
                .fileUrl(fileUrl)
                .cloverCount(cloverCount)
                .build();
    }
}
