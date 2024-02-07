package com.luckkids.domain.push;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertSetting.AlertSetting;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Push extends BaseTimeEntity {

    @Id
    private String deviceId;

    private String pushToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private AlertSetting alertSetting;

    @OneToMany(mappedBy = "push", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<AlertHistory> alertHistory = new ArrayList<>();

    @Builder
    private Push(String deviceId, User user, String pushToken) {
        this.deviceId = deviceId;
        this.user = user;
        this.pushToken = pushToken;
    }

    public static Push of(String deviceId, User user, String pushToken) {
        return Push.builder()
            .deviceId(deviceId)
            .user(user)
            .pushToken(pushToken)
            .build();
    }

    public void updatePushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public void updateUser(User user) {
        this.user = user;
        user.getPushes().add(this);
    }
}
