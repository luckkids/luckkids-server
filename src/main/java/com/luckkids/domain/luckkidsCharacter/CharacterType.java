package com.luckkids.domain.luckkidsCharacter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterType {

    CLOVER("클로버"),
    CLOUD("구름"),
    ROCK("바위"),
    RABBIT("토끼"),
    SUN("해");

    private final String text;
}
