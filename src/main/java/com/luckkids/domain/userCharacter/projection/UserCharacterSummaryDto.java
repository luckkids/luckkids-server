package com.luckkids.domain.userCharacter.projection;

import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.userCharacter.CharacterProgressStatus;

public record UserCharacterSummaryDto(
    CharacterType characterType,
    int level,
    CharacterProgressStatus characterProgressStatus
) {
}
