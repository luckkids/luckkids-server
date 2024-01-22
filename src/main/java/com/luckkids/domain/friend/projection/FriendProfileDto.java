package com.luckkids.domain.friend.projection;

public record FriendProfileDto(
    int friendId,
    String nickname,
    String luckPhrase,
    String fileUrl,
    int characterCount
) {
}
