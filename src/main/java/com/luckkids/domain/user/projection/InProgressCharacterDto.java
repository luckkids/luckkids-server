package com.luckkids.domain.user.projection;

import com.luckkids.domain.luckkidsCharacter.CharacterType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InProgressCharacterDto {

	private CharacterType characterType;
	private int level;
}
