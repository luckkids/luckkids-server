package com.luckkids.domain.user;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.jwt.dto.JwtToken;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

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

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SettingStatus settingStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Push> pushes = new ArrayList<>();

    @Builder
    public User(String email, String password, SnsType snsType, String phoneNumber, Role role, SettingStatus settingStatus) {
        this.email = email;
        this.password = password;
        this.snsType = snsType;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.settingStatus = settingStatus;
    }

    public void checkSnsType(){
        if(snsType.getText().equals(SnsType.APPLE.getText())){
            throw new LuckKidsException(ErrorCode.USER_APPLE);
        }
        else if(snsType.getText().equals(SnsType.KAKAO.getText())){
            throw new LuckKidsException(ErrorCode.USER_KAKAO);
        }
        else if(snsType.getText().equals(SnsType.GOOGLE.getText())){
            throw new LuckKidsException(ErrorCode.USER_GOOGLE);
        }
    }

    /*
    * 비밀번호 체크
    */
    public void checkPassword(String password){
        if(!this.password.equals(password)){
            throw new LuckKidsException(ErrorCode.USER_PASSWORD);
        }
    }

    /*
    * List<RefreshToken>에 요청값으로 받은 deviceId와 일치하는 기존 RefreshToken이 있는지 조회 후 수정 혹은 등록한다.
    */
    public void checkRefreshToken(JwtToken jwtToken, String deviceId) {
        //LAZY속성이기 때문에 사실 refreshToken이 select되지 않았기 때문에 초기화 시켜줌
        Hibernate.initialize(refreshTokens);

        // deviceId와 일치하는 RefreshToken 찾기
        RefreshToken existToken = refreshTokens.stream()
            .filter(refreshToken -> deviceId.equals(refreshToken.getDeviceId()))
            .findFirst()
            .orElse(null);

        // deviceId와 일치하는 RefreshToken이 이미 존재하는 경우, 해당 토큰 업데이트
        if (existToken != null) {
            existToken.updateRefreshToken(jwtToken.getRefreshToken());
        }
        else {
            /*
             * deviceId와 일치하는 RefreshToken이 없는 경우, 새로운 RefreshToken 생성 후
             * 새로운 RefreshToken을 저장
             */
            refreshTokens.add(
                RefreshToken.of(this,jwtToken.getRefreshToken(),deviceId)
            );
        }
    }

    public void checkPushKey(String pushToken, String deviceId) {
        //LAZY속성이기 때문에 사실 Push가 select되지 않았기 때문에 초기화 시켜줌
        Hibernate.initialize(pushes);

        // deviceId와 일치하는 Push 찾기
        Push existPush = pushes.stream()
            .filter(push -> deviceId.equals(push.getDeviceId()))
            .findFirst()
            .orElse(null);

        // deviceId와 일치하는 Push가 이미 존재하는 경우, 해당 PushToken 업데이트
        if (existPush != null) {
            existPush.updatePushToken(pushToken);
        }
        else {
            /*
             * deviceId와 일치하는 Push가 없는 경우, 새로운 Push 생성 후
             * Push리스트에 add
             */
            pushes.add(
                Push.of(this,pushToken,deviceId)
            );
        }
    }

    /**
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     * private List<Mission> missions = new ArrayList<>();
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     * private List<MissionComplete> missionCompletes = new ArrayList<>();
     * @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
     * private List<Friend> friends = new ArrayList<>();
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     * private List<AlertHistory> alertHistories = new ArrayList<>();
     **/

}
