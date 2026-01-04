package com.luckkids.api.service.fortuneTestHistory.request;

import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FortuneTestHistoryCreateServiceRequest {

	private String nickname;
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryCreateServiceRequest(String nickname, FortuneTestResultType resultType) {
		this.nickname = nickname;
		this.resultType = resultType;
	}
}
