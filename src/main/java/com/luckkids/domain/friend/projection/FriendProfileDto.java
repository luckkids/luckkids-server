package com.luckkids.domain.friend.projection;

public record FriendProfileDto(
    int friendId,
    String nickname,
    String luckPhrases,
    String fileUrl,
    int characterCount
) {
}