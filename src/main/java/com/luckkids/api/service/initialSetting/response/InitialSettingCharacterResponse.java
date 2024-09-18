package com.luckkids.api.service.initialSetting.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialSettingCharacterResponse {

	private int id;
	private String nickName;

	@Builder
	private InitialSettingCharacterResponse(int id, String nickName) {
		this.id = id;
		this.nickName = nickName;
	}
}
