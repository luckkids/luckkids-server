package com.luckkids.domain.friends.projection;

import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;

public record FriendProfileReadDto(
    String phraseDescription,
    String fileUrl,
    String characterName,
    int level
)
{
    public FriendProfileReadResponse toServiceResponse(){
        return FriendProfileReadResponse.builder()
            .phraseDescription(phraseDescription)
            .fileUrl(fileUrl)
            .characterName(characterName)
            .level(level)
            .build();
    }
}
