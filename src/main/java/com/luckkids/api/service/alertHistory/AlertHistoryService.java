package com.luckkids.api.service.alertHistory;

import com.luckkids.api.service.alertHistory.response.AlertHistoryStatusResponse;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.luckkids.domain.alertHistory.AlertHistoryStatus.CHECKED;

@Transactional
@RequiredArgsConstructor
@Service
public class AlertHistoryService {

    private final AlertHistoryReadService alertHistoryReadService;

    public AlertHistoryStatusResponse updateAlertHistoryStatus(Long id) {
        AlertHistory alertHistory = alertHistoryReadService.findByOne(id);
        AlertHistoryStatus alertHistoryStatus = CHECKED;
        alertHistory.updateAlertHistoryStatus(alertHistoryStatus);

        return AlertHistoryStatusResponse.of(alertHistoryStatus);
    }
}
