package com.luckkids.domain.luckkidsCharacter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LuckkidsCharacterRepository extends JpaRepository<LuckkidsCharacter, Integer> {

    List<LuckkidsCharacter> findAllByLevel(int level);

    LuckkidsCharacter findByCharacterTypeAndLevel(CharacterType characterType, int level);
}
