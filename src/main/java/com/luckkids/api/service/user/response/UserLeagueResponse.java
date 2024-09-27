package com.luckkids.api.service.user.response;

import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.user.projection.UserLeagueDto;

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

	public static UserLeagueResponse withoutNickname(UserLeagueDto dto) {
		return UserLeagueResponse.builder()
			.nickname(null)
			.characterType(dto.getCharacterType())
			.level(dto.getLevel())
			.characterCount(dto.getCharacterCount())
			.build();
	}

	public static UserLeagueResponse withNickname(UserLeagueDto dto) {
		return UserLeagueResponse.builder()
			.nickname(dto.getNickname())
			.characterType(dto.getCharacterType())
			.level(dto.getLevel())
			.characterCount(dto.getCharacterCount())
			.build();
	}
}
