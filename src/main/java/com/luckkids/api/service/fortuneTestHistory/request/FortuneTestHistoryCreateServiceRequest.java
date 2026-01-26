package com.luckkids.api.service.fortuneTestHistory.request;

import com.luckkids.domain.fortuneTestHistory.FortuneTestHistory;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FortuneTestHistoryCreateServiceRequest {

	private String uuid;
	private String nickname;
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryCreateServiceRequest(String uuid, String nickname, FortuneTestResultType resultType) {
		this.uuid = uuid;
		this.nickname = nickname;
		this.resultType = resultType;
	}

	public FortuneTestHistory toEntity() {
		return FortuneTestHistory.builder()
			.uuid(uuid)
			.nickname(nickname)
			.resultType(resultType)
			.build();
	}
}
