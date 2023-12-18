package com.luckkids.domain.initialCharacter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class InitialCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String characterName;

    private String fileName;

    @Builder
    private InitialCharacter(String characterName, String fileName) {
        this.characterName = characterName;
        this.fileName = fileName;
    }
}
