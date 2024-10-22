package com.luckkids.api.service.user.response;

import com.luckkids.domain.luckkidsCharacter.CharacterType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLeagueResponse {
	private String nickname;
	private CharacterType characterType;
	private int level;
	private int characterCount;

	@Builder
	private UserLeagueResponse(String nickname, CharacterType characterType, int level, int characterCount) {
		this.nickname = nickname;
		this.characterType = characterType;
		this.level = level;
		this.characterCount = characterCount;
	}
}
