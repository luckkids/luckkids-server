package com.luckkids.api.service.missionOutcome.response;

import com.luckkids.domain.luckkidsCharacter.CharacterType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeUpdateResponse {

	private boolean levelUpResult;
	private CharacterType characterType;
	private int level;

	@Builder
	public MissionOutcomeUpdateResponse(boolean levelUpResult, CharacterType characterType, int level) {
		this.levelUpResult = levelUpResult;
		this.characterType = characterType;
		this.level = level;
	}

	public static MissionOutcomeUpdateResponse of(boolean levelUpResult, CharacterType characterType, int level) {
		return MissionOutcomeUpdateResponse.builder()
			.levelUpResult(levelUpResult)
			.characterType(characterType)
			.level(level)
			.build();
	}
}
