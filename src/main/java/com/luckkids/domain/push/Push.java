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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String deviceId;

    private String pushToken;

    private String sound;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "push")
    private AlertSetting alertSetting;

    @Builder
    private Push(String deviceId, User user, String pushToken, String sound) {
        this.deviceId = deviceId;
        this.user = user;
        this.pushToken = pushToken;
        this.sound = sound;
    }

    public static Push of(String deviceId, User user, String pushToken) {
        return Push.builder()
            .deviceId(deviceId)
            .user(user)
            .pushToken(pushToken)
            .sound(PushMessage.DEFAULT_SOUND.getText())
            .build();
    }

    public void updatePushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public void updateUser(User user) {
        this.user = user;
        user.getPushes().add(this);
    }

    public void updateSound(String sound){
        this.sound = sound;
    }
}
