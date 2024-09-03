package com.luckkids.domain.user;

import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.userCharacter.Level;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.jwt.dto.JwtToken;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.luckkids.domain.userCharacter.Level.LEVEL_MAX;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    private String luckPhrase;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SettingStatus settingStatus;

    private int missionCount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Push> pushes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCharacter> userCharacter;

    @Builder
    private User(String email, String password, SnsType snsType, String nickname, String luckPhrase, Role role, SettingStatus settingStatus, int missionCount) {
        this.email = email;
        this.password = password;
        this.snsType = snsType;
        this.nickname = nickname;
        this.luckPhrase = luckPhrase;
        this.role = role;
        this.settingStatus = settingStatus;
        this.missionCount = missionCount;
    }

    public void checkSnsType(SnsType snsType) {
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
                    push.updateUser(this);
                }
            );
    }

    public int calculateLevelBasedOnRemainingMissions() {
        int count = this.calculateRemainingMissions();
        return Level.getLevelByScore(count);
    }

    public int calculateRemainingMissions() {
        if (missionCount <= 0) {
            throw new RuntimeException("missionCount가 0이거나 음수입니다.");
        }
        int remainder = missionCount % LEVEL_MAX.getScoreThreshold();
        return remainder == 0 ? LEVEL_MAX.getScoreThreshold() : remainder;
    }

    public double calculateAchievementRate() {
        final int LEVEL_UP_THRESHOLD = 25;
        int missionsToNextLevel = missionCount % LEVEL_UP_THRESHOLD;
        return (double) missionsToNextLevel / LEVEL_UP_THRESHOLD;
    }

    public void updateMissionCount(int count) {
        missionCount += count;
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

    public void updateNickName(String nickName){
        this.nickname = nickName;
    }

    public void checkSettingStatus() {
        if (this.settingStatus.equals(SettingStatus.COMPLETE)) throw new LuckKidsException("이미 초기세팅이 되어있는 사용자입니다.");
    }
}
