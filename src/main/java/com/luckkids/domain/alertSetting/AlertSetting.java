package com.luckkids.domain.alertSetting;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AlertSetting extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private String deviceId;

    @Enumerated(EnumType.STRING)
    private AlertStatus entire;

    @Enumerated(EnumType.STRING)
    private AlertStatus mission;

    @Enumerated(EnumType.STRING)
    private AlertStatus luck;

    @Enumerated(EnumType.STRING)
    private AlertStatus notice;

    @Builder
    private AlertSetting(User user, String deviceId, AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus notice) {
        this.user = user;
        this.deviceId = deviceId;
        this.entire = entire;
        this.mission = mission;
        this.luck = luck;
        this.notice = notice;
    }

    public void update(AlertType alertType, AlertStatus alertStatus) {
        switch (alertType) {
            case ENTIRE:
                this.entire = alertStatus;
                break;
            case MISSION:
                this.mission = alertStatus;
                break;
            case LUCK:
                this.luck = alertStatus;
                break;
            case NOTICE:
                this.notice = alertStatus;
                break;
        }
    }

    public static AlertSetting of(User user, AlertStatus alertStatus) {
        return AlertSetting.builder()
            .user(user)
            .entire(alertStatus)
            .mission(alertStatus)
            .luck(alertStatus)
            .notice(alertStatus)
            .build();
    }
}
