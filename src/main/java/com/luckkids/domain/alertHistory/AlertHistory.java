package com.luckkids.domain.alertHistory;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
