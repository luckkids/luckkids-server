package com.luckkids.domain.luckkidsCharacter;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Embeddable
public class CharacterId implements Serializable {
    private String characterName;
    private int level;

    @Builder
    private CharacterId(String characterName, int level) {
        this.characterName = characterName;
        this.level = level;
    }

}
