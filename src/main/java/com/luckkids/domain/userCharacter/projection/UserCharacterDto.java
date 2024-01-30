package com.luckkids.domain.userCharacter.projection;

public record UserCharacterDto(
    int userCharacterId,
    String imageFile,
    String lottieFile,
    int level
) {
}
