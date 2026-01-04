package com.luckkids.api.service.fortuneTestHistory;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.fortuneTestHistory.request.FortuneTestHistoryCreateServiceRequest;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistory;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FortuneTestHistoryService {

	private final FortuneTestHistoryRepository fortuneTestHistoryRepository;

	public FortuneTestHistoryResponse createFortuneTestHistory(FortuneTestHistoryCreateServiceRequest request) {
		FortuneTestHistory history = FortuneTestHistory.builder()
			.nickname(request.getNickname())
			.resultType(request.getResultType())
			.build();

		FortuneTestHistory savedHistory = fortuneTestHistoryRepository.save(history);

		return FortuneTestHistoryResponse.of(savedHistory);
	}

	@Transactional(readOnly = true)
	public List<FortuneTestHistoryResponse> findAll() {
		return fortuneTestHistoryRepository.findAll().stream()
			.map(FortuneTestHistoryResponse::of)
			.toList();
	}
}
