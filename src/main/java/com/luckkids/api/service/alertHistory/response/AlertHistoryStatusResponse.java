package com.luckkids.api.service.alertHistory.response;

import com.luckkids.domain.alertHistory.AlertHistoryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlertHistoryStatusResponse {
    private AlertHistoryStatus alertHistoryStatus;

    @Builder
    private AlertHistoryStatusResponse(AlertHistoryStatus alertHistoryStatus) {
        this.alertHistoryStatus = alertHistoryStatus;
    }

    public static AlertHistoryStatusResponse of(AlertHistoryStatus alertHistoryStatus) {
        return AlertHistoryStatusResponse.builder()
            .alertHistoryStatus(alertHistoryStatus)
            .build();
    }
}
