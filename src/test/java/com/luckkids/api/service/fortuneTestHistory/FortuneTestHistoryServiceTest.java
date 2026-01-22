package com.luckkids.api.service.fortuneTestHistory;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.fortuneTestHistory.request.FortuneTestHistoryCreateServiceRequest;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistory;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistoryRepository;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

class FortuneTestHistoryServiceTest extends IntegrationTestSupport {

	@Autowired
	private FortuneTestHistoryService fortuneTestHistoryService;

	@Autowired
	private FortuneTestHistoryRepository fortuneTestHistoryRepository;

	@AfterEach
	void tearDown() {
		fortuneTestHistoryRepository.deleteAllInBatch();
	}

	@DisplayName("닉네임과 운세결과를 받아 운세 테스트 결과를 저장한다.")
	@Test
	void createFortuneTestHistory() {
		// given
		FortuneTestHistoryCreateServiceRequest request = FortuneTestHistoryCreateServiceRequest.builder()
			.nickname("럭키즈")
			.resultType(FortuneTestResultType.TOKKINGI)
			.build();

		// when
		FortuneTestHistoryResponse response = fortuneTestHistoryService.createFortuneTestHistory(request);

		// then
		assertThat(response)
			.extracting("nickname", "resultType")
			.contains("럭키즈", FortuneTestResultType.TOKKINGI);

		List<FortuneTestHistory> histories = fortuneTestHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
			.extracting("nickname", "resultType")
			.containsExactly(tuple("럭키즈", FortuneTestResultType.TOKKINGI));
	}
}