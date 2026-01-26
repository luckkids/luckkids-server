package com.luckkids.api.service.fortuneTestHistory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistory;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistoryRepository;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;

class FortuneTestHistoryReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private FortuneTestHistoryReadService fortuneTestHistoryReadService;

	@Autowired
	private FortuneTestHistoryRepository fortuneTestHistoryRepository;

	@AfterEach
	void tearDown() {
		fortuneTestHistoryRepository.deleteAllInBatch();
	}

	@DisplayName("저장된 운세 테스트 결과를 조회한다.")
	@Test
	void getFortuneTestHistories() {
		// given
		FortuneTestHistory history1 = FortuneTestHistory.builder()
			.uuid("3a9ff30a-6222-4738-b215-5089d77f475e")
			.nickname("행운이")
			.resultType(FortuneTestResultType.TOKKINGI)
			.build();

		fortuneTestHistoryRepository.save(history1);

		// when
		FortuneTestHistoryResponse responses = fortuneTestHistoryReadService.findByUuid(
			"3a9ff30a-6222-4738-b215-5089d77f475e");

		// then
		assertThat(responses)
			.extracting("uuid", "nickname", "resultType")
			.contains("3a9ff30a-6222-4738-b215-5089d77f475e", "행운이", FortuneTestResultType.TOKKINGI);
	}
}