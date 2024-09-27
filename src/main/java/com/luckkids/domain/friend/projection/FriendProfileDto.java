package com.luckkids.domain.friend.projection;

import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.userCharacter.Level;

import lombok.Getter;

@Getter
public class FriendProfileDto {
	private int friendId;
	private String nickname;
	private String luckPhrase;
	private CharacterType characterType;
	private int level;
	private int characterCount;

	public FriendProfileDto(int friendId, String nickname, String luckPhrase, CharacterType characterType, int level,
		int missionCount) {
		this.friendId = friendId;
		this.nickname = nickname;
		this.luckPhrase = luckPhrase;
		this.characterType = characterType;
		this.level = level;
		this.characterCount = missionCount / Level.LEVEL_MAX.getScoreThreshold();
	}
}