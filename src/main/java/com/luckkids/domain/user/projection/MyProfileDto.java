package com.luckkids.domain.user.projection;

public record MyProfileDto(
    int myId,
    String nickname,
    String luckPhrases,
    String fileUrl,
    int characterCount
) {
}