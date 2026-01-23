package com.luckkids.api.service.fortuneTestHistory;

import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import com.luckkids.domain.fortuneTestHistory.FortuneTestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FortuneTestHistoryReadService {

    private final FortuneTestHistoryRepository fortuneTestHistoryRepository;

    public FortuneTestHistoryResponse findByUuid(String uuid) {
        return FortuneTestHistoryResponse.of(fortuneTestHistoryRepository.findByUuid(uuid)
                .orElseThrow(() -> new LuckKidsException("존재하지 않는 개운법입니다.")));
    }
}
