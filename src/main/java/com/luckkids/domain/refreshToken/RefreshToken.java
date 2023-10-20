package com.luckkids.domain.refreshToken;

import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String refreshToken;

    private String deviceId;

    @Builder
    private RefreshToken(User user, String refreshToken, String deviceId){
        this.user = user;
        this.refreshToken = refreshToken;
        this.deviceId = deviceId;
    }

    public static RefreshToken of(User user, String refreshToken, String deviceId){
        return RefreshToken.builder()
            .user(user)
            .refreshToken(refreshToken)
            .deviceId(deviceId)
            .build();
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
