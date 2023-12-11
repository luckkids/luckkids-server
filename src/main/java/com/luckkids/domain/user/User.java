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
import java.util.Optional;

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

    private int missionCount;

    private String luckPhrases;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SettingStatus settingStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Push> pushes = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserCharacter userCharacter;

    @Builder
    private User(String email, String password, SnsType snsType, int missionCount, String luckPhrases, Role role, SettingStatus settingStatus) {
        this.email = email;
        this.password = password;
        this.snsType = snsType;
        this.missionCount = missionCount;
        this.luckPhrases = luckPhrases;
        this.role = role;
        this.settingStatus = settingStatus;
    }

    public void checkSnsType() {
        snsType.checkSnsType();
    }

    public void loginCheckSnsType(SnsType snsType) {
        if (!this.snsType.equals(snsType)) {
            this.snsType.checkSnsType();
        }
    }

    /*
     * List<RefreshToken>에 요청값으로 받은 deviceId와 일치하는 기존 RefreshToken이 있는지 조회 후 수정 혹은 등록한다.
     */
    public void checkRefreshToken(JwtToken jwtToken, String deviceId) {
        // deviceId와 일치하는 RefreshToken 찾기
        Optional<RefreshToken> existToken = refreshTokens.stream()
            .filter(refreshToken -> deviceId.equals(refreshToken.getDeviceId()))
            .findFirst();

        // deviceId와 일치하는 RefreshToken이 이미 존재하는 경우, 해당 토큰 업데이트
        if (existToken.isPresent()) {
            existToken.get().updateRefreshToken(jwtToken.getRefreshToken());
        } else { // deviceId와 일치하는 RefreshToken이 없는 경우, 새로운 RefreshToken 생성 후 새로운 RefreshToken을 저장
            RefreshToken refreshToken = RefreshToken.of(this, jwtToken.getRefreshToken(), deviceId);
            refreshToken.setUser(this);
        }
    }

    public void checkPushKey(String pushToken, String deviceId) {
        // deviceId와 일치하는 Push 찾기
        Optional<Push> existPush = pushes.stream()
            .filter(push -> deviceId.equals(push.getDeviceId()))
            .findFirst();

        // deviceId와 일치하는 Push가 이미 존재하는 경우, 해당 PushToken 업데이트
        if (existPush.isPresent()) {
            existPush.get().updatePushToken(pushToken);
        } else {// deviceId와 일치하는 Push가 없는 경우, 새로운 Push 생성 후Push리스트에 add
            Push push = Push.of(this, pushToken, deviceId);
            push.setUser(this);
        }
    }

    public void updateLuckPhrases(String luckPhrases){
        this.luckPhrases = luckPhrases;
    }

    public void updatePassword(String password){
        this.password = password;
    }
}
