package com.luckkids.domain.luckkidsCharacter;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;

@Transactional
class LuckkidsCharacterRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private LuckkidsCharacterRepository luckkidsCharacterRepository;

	@DisplayName("캐릭터 타입과 레벨에 맞는 럭키즈 캐릭터를 가져온다.")
	@Test
	void findByCharacterTypeAndLevel() {
		// given
		LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1);
		luckkidsCharacterRepository.save(luckkidsCharacter);

		// when
		LuckkidsCharacter findLuckkidsCharacter = luckkidsCharacterRepository.findByCharacterTypeAndLevel(CLOVER, 1);

		// then
		assertThat(findLuckkidsCharacter)
			.extracting("characterType", "level")
			.contains(CLOVER, 1);

	}

	private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level) {
		return LuckkidsCharacter.builder()
			.characterType(characterType)
			.level(level)
			.build();

	}
}