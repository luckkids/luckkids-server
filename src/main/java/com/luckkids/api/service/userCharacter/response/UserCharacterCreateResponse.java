package com.luckkids.api.service.userCharacter.response;

import com.luckkids.api.service.initialSetting.response.InitialSettingCharacterResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterCreateResponse {

	private int id;
	private String nickName;

	@Builder
	private UserCharacterCreateResponse(int id, String nickName) {
		this.id = id;
		this.nickName = nickName;
	}

	public static UserCharacterCreateResponse of(User user, UserCharacter userCharacter) {
		return UserCharacterCreateResponse.builder()
			.id(userCharacter.getId())
			.nickName(user.getNickname())
			.build();
	}

	public InitialSettingCharacterResponse toInitialSettingResponse() {
		return InitialSettingCharacterResponse.builder()
			.id(id)
			.nickName(nickName)
			.build();
	}

}
