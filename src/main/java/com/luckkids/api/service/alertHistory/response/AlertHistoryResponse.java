package com.luckkids.api.service.alertHistory.response;

import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AlertHistoryResponse {

    private Long id;
    private String alertDescription;
    private AlertHistoryStatus alertHistoryStatus;
    private LocalDateTime createdDate;

    @Builder
    private AlertHistoryResponse(Long id, String alertDescription, AlertHistoryStatus alertHistoryStatus, LocalDateTime createdDate) {
        this.id = id;
        this.alertDescription = alertDescription;
        this.alertHistoryStatus = alertHistoryStatus;
        this.createdDate = createdDate;
    }

    public static AlertHistoryResponse of(AlertHistory alertHistory) {
        return AlertHistoryResponse.builder()
            .id(alertHistory.getId())
            .alertDescription(alertHistory.getAlertDescription())
            .alertHistoryStatus(alertHistory.getAlertHistoryStatus())
            .createdDate(alertHistory.getCreatedDate())
            .build();
    }
}
