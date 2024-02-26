package com.luckkids.api.service.alertHistory;

import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertHistoryService {

    private final AlertHistoryRepository alertHistoryRepository;

    public AlertHistory save(AlertHistoryServiceRequest alertHistoryServiceRequest){
        return alertHistoryRepository.save(alertHistoryServiceRequest.toEntity());
    }
}
