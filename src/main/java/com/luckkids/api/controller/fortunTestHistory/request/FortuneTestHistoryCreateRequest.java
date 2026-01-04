package com.luckkids.api.controller.fortunTestHistory.request;

import com.luckkids.api.service.fortuneTestHistory.request.FortuneTestHistoryCreateServiceRequest;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FortuneTestHistoryCreateRequest {

	@NotBlank(message = "닉네임은 필수입니다.")
	private String nickname;

	@NotNull(message = "운세 결과는 필수입니다.")
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryCreateRequest(String nickname, FortuneTestResultType resultType) {
		this.nickname = nickname;
		this.resultType = resultType;
	}

	public FortuneTestHistoryCreateServiceRequest toServiceRequest() {
		return FortuneTestHistoryCreateServiceRequest.builder()
			.nickname(nickname)
			.resultType(resultType)
			.build();
	}
}
