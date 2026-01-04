package com.luckkids.api.service.fortuneTestHistory.response;

import com.luckkids.domain.fortuneTestHistory.FortuneTestHistory;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FortuneTestHistoryResponse {

	private int id;
	private String nickname;
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryResponse(int id, String nickname, FortuneTestResultType resultType) {
		this.id = id;
		this.nickname = nickname;
		this.resultType = resultType;
	}

	public static FortuneTestHistoryResponse of(FortuneTestHistory history) {
		return FortuneTestHistoryResponse.builder()
			.id(history.getId())
			.nickname(history.getNickname())
			.resultType(history.getResultType())
			.build();
	}
}
