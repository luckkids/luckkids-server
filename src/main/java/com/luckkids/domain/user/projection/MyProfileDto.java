package com.luckkids.domain.user.projection;

public record MyProfileDto(
    int myId,
    String nickname,
    String luckPhrase,
    String fileUrl,
    int characterCount
) {
}