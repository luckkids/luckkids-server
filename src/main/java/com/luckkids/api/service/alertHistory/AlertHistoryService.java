package com.luckkids.api.service.alertHistory;

import static com.luckkids.domain.alertHistory.AlertHistoryStatus.*;

import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.push.PushMessage;
import com.luckkids.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.alertHistory.response.AlertHistoryStatusResponse;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AlertHistoryService {

    private final AlertHistoryRepository alertHistoryRepository;
    private final AlertHistoryReadService alertHistoryReadService;

    public void createWelcomeAlertHistory(User user) {
        alertHistoryRepository.save(AlertHistory.builder()
                .user(user)
                .alertDescription(PushMessage.WELCOME.getText())
                .alertHistoryStatus(AlertHistoryStatus.CHECKED)
                .alertDestinationType(AlertDestinationType.WELCOME)
                .build());
    }

    public AlertHistory createAlertHistory(AlertHistoryServiceRequest alertHistoryServiceRequest) {
        return alertHistoryRepository.save(alertHistoryServiceRequest.toEntity());
    }

    public AlertHistoryStatusResponse updateAlertHistoryStatus(Long id) {
        AlertHistory alertHistory = alertHistoryReadService.findByOne(id);
        alertHistory.updateAlertHistoryStatus(CHECKED);

        return AlertHistoryStatusResponse.of(alertHistory.getAlertHistoryStatus());
    }
}
