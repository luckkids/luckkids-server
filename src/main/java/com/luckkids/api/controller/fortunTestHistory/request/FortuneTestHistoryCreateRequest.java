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

	@NotNull(message = "운세 결과는 필수입니다.")
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistoryCreateRequest(String uuid, FortuneTestResultType resultType) {
		this.uuid = uuid;
		this.resultType = resultType;
	}

	public FortuneTestHistoryCreateServiceRequest toServiceRequest() {
		return FortuneTestHistoryCreateServiceRequest.builder()
			.uuid(uuid)
			.resultType(resultType)
			.build();
	}
}
