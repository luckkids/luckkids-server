package com.luckkids.api.service.userCharacter.response;

import com.luckkids.api.service.missionOutcome.response.MissionOutcomeUpdateResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterLevelUpResponse {

	private boolean levelUpResult;
	private CharacterType characterType;
	private int level;

	@Builder
	private UserCharacterLevelUpResponse(boolean levelUpResult, CharacterType characterType, int level) {
		this.levelUpResult = levelUpResult;
		this.characterType = characterType;
		this.level = level;
	}

	public static UserCharacterLevelUpResponse of(boolean levelUpResult, CharacterType characterType, int level) {
		return UserCharacterLevelUpResponse.builder()
			.levelUpResult(levelUpResult)
			.characterType(characterType)
			.level(level)
			.build();
	}

	public MissionOutcomeUpdateResponse toMissionOutcomeUpdateResponse() {
		return MissionOutcomeUpdateResponse.builder()
			.levelUpResult(this.levelUpResult)
			.characterType(this.characterType)
			.level(this.level)
			.build();
	}
}
