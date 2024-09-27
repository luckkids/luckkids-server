package com.luckkids.domain.user.projection;

import com.luckkids.api.service.user.response.UserLeagueResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.userCharacter.Level;

import lombok.Getter;

@Getter
public class UserLeagueDto {
	private String nickname;
	private CharacterType characterType;
	private int level;
	private int characterCount;

	public UserLeagueDto(String nickname, CharacterType characterType, int level, int missionCount) {
		this.nickname = nickname;
		this.characterType = characterType;
		this.level = level;
		this.characterCount = missionCount / Level.LEVEL_MAX.getScoreThreshold();
	}

	public UserLeagueResponse toUserLeagueResponse() {
		return UserLeagueResponse.builder()
			.nickname(nickname)
			.characterType(characterType)
			.level(level)
			.characterCount(characterCount)
			.build();
	}
}
