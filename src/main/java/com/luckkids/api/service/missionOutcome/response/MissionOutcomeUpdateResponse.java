package com.luckkids.api.service.missionOutcome.response;

import com.luckkids.domain.luckkidsCharacter.CharacterType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeUpdateResponse {

	private boolean levelUpResult;
	private int level;
	private CharacterType characterType;
	private String lottieFile;
	private String imageFile;

	@Builder
	public MissionOutcomeUpdateResponse(boolean levelUpResult, int level, CharacterType characterType,
		String lottieFile, String imageFile) {
		this.levelUpResult = levelUpResult;
		this.level = level;
		this.characterType = characterType;
		this.lottieFile = lottieFile;
		this.imageFile = imageFile;
	}

	public static MissionOutcomeUpdateResponse of(boolean levelUpResult, int level, CharacterType characterType,
		String lottieFile, String imageFile) {
		return MissionOutcomeUpdateResponse.builder()
			.levelUpResult(levelUpResult)
			.level(level)
			.characterType(characterType)
			.lottieFile(lottieFile)
			.imageFile(imageFile)
			.build();
	}
}
