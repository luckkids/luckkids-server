package com.luckkids.api.service.fortuneTestHistory;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

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

	@DisplayName("저장된 모든 운세 테스트 결과들을 조회한다.")
	@Test
	void getFortuneTestHistories() {
		// given
		FortuneTestHistory history1 = FortuneTestHistory.builder()
			.nickname("유저1")
			.resultType(FortuneTestResultType.TOKKINGI)
			.build();
		FortuneTestHistory history2 = FortuneTestHistory.builder()
			.nickname("유저2")
			.resultType(FortuneTestResultType.TAEYANGI)
			.build();

		fortuneTestHistoryRepository.saveAll(List.of(history1, history2));

		// when
		List<FortuneTestHistoryResponse> responses = fortuneTestHistoryReadService.findAll();

		// then
		assertThat(responses).hasSize(2)
			.extracting("nickname", "resultType")
			.containsExactlyInAnyOrder(
				tuple("유저1", FortuneTestResultType.TOKKINGI),
				tuple("유저2", FortuneTestResultType.TAEYANGI)
			);
	}
}