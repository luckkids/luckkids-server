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
	private String uuid;
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryResponse(int id, String uuid, FortuneTestResultType resultType) {
		this.id = id;
		this.uuid = uuid;
		this.resultType = resultType;
	}

	public static FortuneTestHistoryResponse of(FortuneTestHistory history) {
		return FortuneTestHistoryResponse.builder()
			.id(history.getId())
			.uuid(history.getUuid())
			.resultType(history.getResultType())
			.build();
	}
}
