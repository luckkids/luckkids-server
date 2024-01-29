package com.luckkids.domain.alertSetting;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.push.Push;
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

    @OneToOne
    @JoinColumn(name = "device_id")
    private Push push;

    @Enumerated(EnumType.STRING)
    private AlertStatus entire;

    @Enumerated(EnumType.STRING)
    private AlertStatus mission;

    @Enumerated(EnumType.STRING)
    private AlertStatus luck;

    @Enumerated(EnumType.STRING)
    private AlertStatus notice;

    @Builder
    private AlertSetting(Push push, AlertStatus entire, AlertStatus mission, AlertStatus luck, AlertStatus notice) {
        this.push = push;
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

    public static AlertSetting of(Push push, AlertStatus alertStatus){
        return AlertSetting.builder()
            .push(push)
            .entire(alertStatus)
            .mission(alertStatus)
            .luck(alertStatus)
            .notice(alertStatus)
            .build();
    }
}
