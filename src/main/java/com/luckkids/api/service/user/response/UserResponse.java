package com.luckkids.api.service.user.response;

import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {

	private String email;
	private String nickname;
	private SnsType snsType;
	private String luckPhrase;
	private Role role;
	private SettingStatus settingStatus;
	private int missionCount;

	@Builder
	private UserResponse(String email, String nickname, SnsType snsType, String luckPhrase, Role role,
		SettingStatus settingStatus, int missionCount) {
		this.email = email;
		this.nickname = nickname;
		this.snsType = snsType;
		this.luckPhrase = luckPhrase;
		this.role = role;
		this.settingStatus = settingStatus;
		this.missionCount = missionCount;
	}

	public static UserResponse of(User user) {
		return UserResponse.builder()
			.email(user.getEmail())
			.nickname(user.getNickname())
			.snsType(user.getSnsType())
			.luckPhrase(user.getLuckPhrase())
			.role(user.getRole())
			.settingStatus(user.getSettingStatus())
			.missionCount(user.getMissionCount())
			.build();
	}
}
