package com.luckkids.domain.luckkidsCharacter;

import com.luckkids.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"character_type", "level"}))
public class LuckkidsCharacter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "character_type")
    private CharacterType characterType;

    private int level;

    private String lottieFile;

    private String imageFile;

    @Builder
    public LuckkidsCharacter(CharacterType characterType, int level, String lottieFile, String imageFile) {
        this.characterType = characterType;
        this.level = level;
        this.lottieFile = lottieFile;
        this.imageFile = imageFile;
    }
}