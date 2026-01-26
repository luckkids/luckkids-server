package com.luckkids.api.service.fortuneTestHistory.response;

import com.luckkids.domain.fortuneTestHistory.FortuneTestHistory;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FortuneTestHistoryResponse {

	private String uuid;
	private String nickname;
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryResponse(String uuid, String nickname, FortuneTestResultType resultType) {
		this.uuid = uuid;
		this.nickname = nickname;
		this.resultType = resultType;
	}

	public static FortuneTestHistoryResponse of(FortuneTestHistory history) {
		return FortuneTestHistoryResponse.builder()
			.uuid(history.getUuid())
			.nickname(history.getNickname())
			.resultType(history.getResultType())
			.build();
	}
}
