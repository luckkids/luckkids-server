package com.luckkids.domain.alertHistory;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.push.Push;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AlertHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Push push;

    private String alertDescription;

    @Enumerated(EnumType.STRING)
    private AlertHistoryStatus alertHistoryStatus;

    @Builder
    private AlertHistory(Push push, String alertDescription, AlertHistoryStatus alertHistoryStatus) {
        this.push = push;
        this.alertDescription = alertDescription;
        this.alertHistoryStatus = alertHistoryStatus;
    }

    public void updateAlertHistoryStatus(AlertHistoryStatus alertHistoryStatus) {
        this.alertHistoryStatus = alertHistoryStatus;
    }
}
