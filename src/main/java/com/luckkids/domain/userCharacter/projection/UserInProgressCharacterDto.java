package com.luckkids.domain.userCharacter.projection;

import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.userCharacter.UserCharacter;

public record UserInProgressCharacterDto(
    UserCharacter userCharacter,
    LuckkidsCharacter luckkidsCharacter
) {
}
