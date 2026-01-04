package com.luckkids.api.service.fortuneTestHistory;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistoryRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FortuneTestHistoryReadService {

	private final FortuneTestHistoryRepository fortuneTestHistoryRepository;

	public List<FortuneTestHistoryResponse> findAll() {
		return fortuneTestHistoryRepository.findAll().stream()
			.map(FortuneTestHistoryResponse::of)
			.toList();
	}
}
