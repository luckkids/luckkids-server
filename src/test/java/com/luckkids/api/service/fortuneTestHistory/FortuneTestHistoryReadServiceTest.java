package com.luckkids.api.service.fortuneTestHistory;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistory;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistoryRepository;
import com.luckkids.domain.fortuneTestHistory.FortuneTestResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
                .uuid("유저1")
                .resultType(FortuneTestResultType.TOKKINGI)
                .build();

        fortuneTestHistoryRepository.save(history1);

        // when
        FortuneTestHistoryResponse responses = fortuneTestHistoryReadService.findByUuid("유저1");

        // then
        assertThat(responses)
                .extracting("uuid", "resultType")
                .contains("유저1", FortuneTestResultType.TOKKINGI);
    }
}