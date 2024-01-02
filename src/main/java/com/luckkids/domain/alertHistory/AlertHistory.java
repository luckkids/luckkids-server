package com.luckkids.domain.alertHistory;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.user.User;
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
    private User user;

    private String alertDescription;

    @Enumerated(EnumType.STRING)
    private AlertHistoryStatus alertHistoryStatus;

    @Builder
    private AlertHistory(User user, String alertDescription, AlertHistoryStatus alertHistoryStatus) {
        this.user = user;
        this.alertDescription = alertDescription;
        this.alertHistoryStatus = alertHistoryStatus;
    }

}
