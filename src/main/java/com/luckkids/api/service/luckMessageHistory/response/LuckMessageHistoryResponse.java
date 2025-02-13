package com.luckkids.api.service.luckMessageHistory.response;

import com.luckkids.domain.luckMessageHistory.LuckMessageHistory;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LuckMessageHistoryResponse {
	private String messageDescription;

	@Builder
	public LuckMessageHistoryResponse(String messageDescription) {
		this.messageDescription = messageDescription;
	}

	public static LuckMessageHistoryResponse of(LuckMessageHistory luckMessageHistory) {
		return LuckMessageHistoryResponse.builder()
			.messageDescription(luckMessageHistory.getMessageDescription())
			.build();
	}
}
