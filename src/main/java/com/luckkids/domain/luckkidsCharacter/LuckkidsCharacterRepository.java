package com.luckkids.domain.luckkidsCharacter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LuckkidsCharacterRepository extends JpaRepository<LuckkidsCharacter, Integer> {

    List<LuckkidsCharacter> findAllByLevel(int level);

    @Query("select l from LuckkidsCharacter l where l.level = 1 order by rand() limit 1")
    Optional<LuckkidsCharacter> findRandomLevel1();

    LuckkidsCharacter findByCharacterTypeAndLevel(CharacterType characterType, int level);
}
