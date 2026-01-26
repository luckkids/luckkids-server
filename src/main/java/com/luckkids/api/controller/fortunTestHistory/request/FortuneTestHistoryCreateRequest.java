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

	@NotBlank(message = "사용자 UUID는 필수입니다.")
	private String uuid;

	@NotBlank(message = "사용자 닉네임은 필수입니다.")
	private String nickname;

	@NotNull(message = "운세 결과는 필수입니다.")
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryCreateRequest(String uuid, String nickname, FortuneTestResultType resultType) {
		this.uuid = uuid;
		this.nickname = nickname;
		this.resultType = resultType;
	}

	public FortuneTestHistoryCreateServiceRequest toServiceRequest() {
		return FortuneTestHistoryCreateServiceRequest.builder()
			.uuid(uuid)
			.nickname(nickname)
			.resultType(resultType)
			.build();
	}
}
