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
	private int level;
	private CharacterType characterType;
	private String lottieFile;
	private String imageFile;

	@Builder
	private UserCharacterLevelUpResponse(boolean levelUpResult, int level, CharacterType characterType,
		String lottieFile, String imageFile) {
		this.levelUpResult = levelUpResult;
		this.level = level;
		this.characterType = characterType;
		this.lottieFile = lottieFile;
		this.imageFile = imageFile;
	}

	public static UserCharacterLevelUpResponse of(boolean levelUpResult, int level, CharacterType characterType,
		String lottieFile, String imageFile) {
		return UserCharacterLevelUpResponse.builder()
			.levelUpResult(levelUpResult)
			.level(level)
			.characterType(characterType)
			.lottieFile(lottieFile)
			.imageFile(imageFile)
			.build();
	}

	public MissionOutcomeUpdateResponse toMissionOutcomeUpdateResponse() {
		return MissionOutcomeUpdateResponse.builder()
			.levelUpResult(this.levelUpResult)
			.level(this.level)
			.characterType(this.characterType)
			.lottieFile(this.lottieFile)
			.imageFile(this.imageFile)
			.build();
	}
}
