package com.luckkids.api.service.luckkidsCharacter.response;

import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LuckCharacterRandResponse {

    private int id;
    private CharacterType characterType;
    private int level;
    private String lottieFile;
    private String imageFile;

    @Builder
    private LuckCharacterRandResponse(int id, CharacterType characterType, int level, String lottieFile, String imageFile) {
        this.id = id;
        this.characterType = characterType;
        this.level = level;
        this.lottieFile = lottieFile;
        this.imageFile = imageFile;
    }

    public static LuckCharacterRandResponse of(LuckkidsCharacter character) {
        return LuckCharacterRandResponse.builder()
            .id(character.getId())
            .characterType(character.getCharacterType())
            .lottieFile(character.getLottieFile())
            .imageFile(character.getImageFile())
            .build();
    }
}
