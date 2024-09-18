package com.luckkids.api.service.luckkidsCharacter.response;

import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LuckkidsCharacterRandResponse {

	private int id;
	private CharacterType characterType;
	private int level;

	@Builder
	private LuckkidsCharacterRandResponse(int id, CharacterType characterType, int level) {
		this.id = id;
		this.characterType = characterType;
		this.level = level;
	}

	public static LuckkidsCharacterRandResponse of(LuckkidsCharacter character) {
		return LuckkidsCharacterRandResponse.builder()
			.id(character.getId())
			.characterType(character.getCharacterType())
			.level(character.getLevel())
			.build();
	}
}
