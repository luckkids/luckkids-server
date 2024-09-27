package com.luckkids.domain.user.projection;

import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.userCharacter.Level;

import lombok.Getter;

@Getter
public class MyProfileDto {
	private int myId;
	private String nickname;
	private String luckPhrase;
	private CharacterType characterType;
	private int level;
	private int CharacterCount;

	public MyProfileDto(int myId, String nickname, String luckPhrase, CharacterType characterType, int level,
		int missionCount) {
		this.myId = myId;
		this.nickname = nickname;
		this.luckPhrase = luckPhrase;
		this.characterType = characterType;
		this.level = level;
		this.CharacterCount = missionCount / Level.LEVEL_MAX.getScoreThreshold();
	}
}