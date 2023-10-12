package com.luckkids.domain.user;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.BaseEntity;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.security.dto.JwtToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    private String phoneNumber;

    private String Profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

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
    * */
    public void checkPassword(String password){
        if(!this.password.equals(password)){
            throw new LuckKidsException(ErrorCode.USER_PASSWORD);
        }
    }

    /*
    * List<RefreshToken>에 요청값으로 받은 deviceId와 일치하는 기존 RefreshToken이 있는지 조회 후 수정 혹은 등록한다.
    * */
    public void checkRefreshToken(JwtToken jwtToken, String deviceId){
        Hibernate.initialize(refreshTokens);
        // deviceId와 일치하는 RefreshToken 찾기
        RefreshToken existToken = refreshTokens.stream()
                .filter(refreshToken -> deviceId.equals(refreshToken.getDeviceId()))
                .findFirst()
                .orElse(null);

        if (existToken != null) {
            // deviceId와 일치하는 RefreshToken이 이미 존재하는 경우, 해당 토큰 업데이트
            existToken.updateRefreshToken(jwtToken.getRefreshToken());
        } else {
            // deviceId와 일치하는 RefreshToken이 없는 경우, 새로운 RefreshToken 생성
            RefreshToken newToken = new RefreshToken(this, jwtToken.getRefreshToken(), deviceId);
            // 새로운 RefreshToken을 저장
            refreshTokens.add(newToken);
        }
    }
}
