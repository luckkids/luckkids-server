package com.luckkids.domain.luckkidsCharacter.projection;

import com.luckkids.domain.luckkidsCharacter.CharacterType;

public record LuckkidsCharacterDto(
    int userCharacterId,
    CharacterType characterType,
    String lottieFile,
    String imageFile) {
}


