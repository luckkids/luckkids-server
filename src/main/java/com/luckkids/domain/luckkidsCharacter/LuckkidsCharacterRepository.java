package com.luckkids.domain.luckkidsCharacter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LuckkidsCharacterRepository extends JpaRepository<LuckkidsCharacter, CharacterId> {

    List<LuckkidsCharacter> findAllByCharacterIdLevel(int level);

}
