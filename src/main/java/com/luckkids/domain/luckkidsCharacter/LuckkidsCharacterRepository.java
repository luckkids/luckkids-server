package com.luckkids.domain.luckkidsCharacter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LuckkidsCharacterRepository extends JpaRepository<LuckkidsCharacter, Integer> {

	@Query("select l from LuckkidsCharacter l where l.level = 1 order by rand() limit 1")
	Optional<LuckkidsCharacter> findRandomLevel1();

	LuckkidsCharacter findByCharacterTypeAndLevel(CharacterType characterType, int level);
}
