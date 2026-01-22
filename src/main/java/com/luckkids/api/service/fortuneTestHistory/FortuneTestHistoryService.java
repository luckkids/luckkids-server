package com.luckkids.api.service.fortuneTestHistory;

import com.luckkids.api.service.fortuneTestHistory.request.FortuneTestHistoryCreateServiceRequest;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FortuneTestHistoryService {

    private final FortuneTestHistoryRepository fortuneTestHistoryRepository;

    public FortuneTestHistoryResponse createFortuneTestHistory(FortuneTestHistoryCreateServiceRequest request) {
        return FortuneTestHistoryResponse.of(fortuneTestHistoryRepository.save(request.toEntity()));
    }
}
