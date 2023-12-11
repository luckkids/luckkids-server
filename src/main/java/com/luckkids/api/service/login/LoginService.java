package com.luckkids.api.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.api.client.apple.AppleApiClient;
import com.luckkids.api.client.google.GoogleApiClient;
import com.luckkids.api.client.kakao.KakaoApiClient;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.request.OAuthLoginServiceRequest;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.api.service.login.response.OAuthLoginResponse;
import com.luckkids.domain.user.*;
import com.luckkids.jwt.JwtTokenGenerator;
import com.luckkids.jwt.dto.JwtToken;
import com.luckkids.jwt.dto.UserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final KakaoApiClient kakaoApiClient;
    private final GoogleApiClient googleApiClient;
    private final AppleApiClient appleApiClient;

    public LoginResponse normalLogin(LoginServiceRequest loginServiceRequest) throws JsonProcessingException {
        User user = userRepository.findByEmail(loginServiceRequest.getEmail());     //회원조회

        Optional.ofNullable(user).orElseThrow(()->new LuckKidsException(ErrorCode.USER_UNKNOWN));

        user.loginCheckSnsType(SnsType.NORMAL);                                     //SNS가입여부확인

        user.checkPassword(loginServiceRequest.getPassword());                      //비밀번호 체크

        return LoginResponse.of(user, setJwtTokenPushKey(user, loginServiceRequest.getDeviceId(), loginServiceRequest.getPushKey()));
    }

    public OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws JsonProcessingException {
        String email = switch (oAuthLoginServiceRequest.getSnsType()) {
            case KAKAO -> kakaoApiClient.getEmail(oAuthLoginServiceRequest.getCode());
            case GOOGLE -> googleApiClient.getEmail(oAuthLoginServiceRequest.getCode());
            case APPLE -> appleApiClient.getEmail(oAuthLoginServiceRequest.getCode());
            default -> throw new LuckKidsException(ErrorCode.OAUTH_UNKNOWN);
        };
        User user = Optional.ofNullable(userRepository.findByEmail(email))
            .orElseGet(() ->
                userRepository.save(User.builder()
                    .email(email)
                    .missionCount(0)
                    .snsType(oAuthLoginServiceRequest.getSnsType())
                    .role(Role.USER)
                    .settingStatus(SettingStatus.INCOMPLETE)
                    .build())
            );

        user.loginCheckSnsType(oAuthLoginServiceRequest.getSnsType());              //SNS가입여부확인

        return OAuthLoginResponse.of(user, setJwtTokenPushKey(user, oAuthLoginServiceRequest.getDeviceId(), oAuthLoginServiceRequest.getPushKey()));
    }

    private JwtToken setJwtTokenPushKey(User user, String deviceId, String pushKey) throws JsonProcessingException {
        UserInfo userInfo = UserInfo.of(user.getId(), user.getEmail());
        JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);                   //JWT토큰생성
        user.checkRefreshToken(jwtToken, deviceId);                                 //deviceId로 기존 refreshToken 조회 후 수정 혹은 등록
        user.checkPushKey(pushKey, deviceId);                                       //deviceId로 기존 push 조회 후 수정 혹은 등록
        return jwtToken;
    }
}
