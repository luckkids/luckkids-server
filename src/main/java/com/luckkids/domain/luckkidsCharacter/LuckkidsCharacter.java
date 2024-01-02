package com.luckkids.domain.luckkidsCharacter;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class LuckkidsCharacter {

    @EmbeddedId
    private CharacterId characterId;
    private String fileName;

    @Builder
    private LuckkidsCharacter(String fileName, CharacterId characterId) {
        this.fileName = fileName;
        this.characterId = characterId;
    }
}