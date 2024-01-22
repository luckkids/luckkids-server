package com.luckkids.domain.luckkidsCharacter;

import com.luckkids.domain.BaseTimeEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class LuckkidsCharacter extends BaseTimeEntity {

    @EmbeddedId
    private CharacterId characterId;
    private String file;

    @Builder
    private LuckkidsCharacter(String file, CharacterId characterId) {
        this.file = file;
        this.characterId = characterId;
    }
}