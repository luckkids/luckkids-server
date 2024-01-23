package com.luckkids.domain.user;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.jwt.dto.JwtToken;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    private String nickname;

    private String luckPhrase;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SettingStatus settingStatus;

    private int missionCount;

    private int characterCount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Push> pushes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCharacter> userCharacter;

    @Builder
    private User(String email, String password, SnsType snsType, String nickname, String luckPhrase, Role role, SettingStatus settingStatus, int missionCount, int characterCount) {
        this.email = email;
        this.password = password;
        this.snsType = snsType;
        this.nickname = nickname;
        this.luckPhrase = luckPhrase;
        this.role = role;
        this.settingStatus = settingStatus;
        this.missionCount = missionCount;
        this.characterCount = characterCount;
    }

    public void CheckSnsTypeForLogin(SnsType snsType) {
        if (!this.snsType.equals(snsType)) {
            this.snsType.checkSnsType();
        }
    }

    public void checkRefreshToken(JwtToken jwtToken, String deviceId) {
        refreshTokens.stream()
            .filter(refreshToken -> deviceId.equals(refreshToken.getDeviceId()))
            .findFirst()
            .ifPresentOrElse(
                existToken -> existToken.updateRefreshToken(jwtToken.getRefreshToken()),
                () -> {
                    RefreshToken refreshToken = RefreshToken.of(this, jwtToken.getRefreshToken(), deviceId);
                    refreshToken.setUser(this);
                }
            );
    }

    public void checkPushKey(String pushToken, String deviceId) {
        pushes.stream()
            .filter(push -> deviceId.equals(push.getDeviceId()))
            .findFirst()
            .ifPresentOrElse(
                existPush -> existPush.updatePushToken(pushToken),
                () -> {
                    Push push = Push.of(deviceId, this, pushToken);
                    push.setUser(this);
                }
            );
    }

    public int updateMissionCount(int count) {
        missionCount += count;
        return missionCount;
    }

    public void updateLuckPhrase(String luckPhrase) {
        this.luckPhrase = luckPhrase;
    }

    public void updateSettingStatus(SettingStatus settingStatus) {
        this.settingStatus = settingStatus;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
