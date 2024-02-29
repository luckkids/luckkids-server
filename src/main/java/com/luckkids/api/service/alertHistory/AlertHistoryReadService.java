package com.luckkids.api.service.alertHistory;

import com.luckkids.api.service.alertHistory.request.AlertHistoryDeviceIdServiceRequest;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AlertHistoryReadService {

    private final AlertHistoryRepository alertHistoryRepository;

    public AlertHistory findByOne(Long id) {
        return alertHistoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 알림 내역은 없습니다. id = " + id));
    }

    public AlertHistoryResponse getAlertHistory(AlertHistoryDeviceIdServiceRequest request) {
        AlertHistory alertHistory = alertHistoryRepository.findByDeviceId(request.getDeviceId());
        return AlertHistoryResponse.of(alertHistory);
    }
}
