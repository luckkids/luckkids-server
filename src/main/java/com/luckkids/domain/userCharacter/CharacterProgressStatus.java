package com.luckkids.domain.userCharacter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterProgressStatus {

    IN_PROGRESS("진행중"),
    COMPLETED("완료");

    private final String text;
}
