package com.luckkids.api.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.api.client.OAuthApiClient;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.login.request.LoginGenerateTokenServiceRequest;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.request.OAuthLoginServiceRequest;
import com.luckkids.api.service.login.response.LoginGenerateTokenResponse;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.api.service.login.response.OAuthLoginResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.domain.user.*;
import com.luckkids.jwt.JwtTokenGenerator;
import com.luckkids.jwt.dto.JwtToken;
import com.luckkids.jwt.dto.LoginUserInfo;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserReadService userReadService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Map<SnsType, OAuthApiClient> clients;

    public LoginService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtTokenGenerator jwtTokenGenerator, UserReadService userReadService, BCryptPasswordEncoder bCryptPasswordEncoder, List<OAuthApiClient> clients) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.userReadService = userReadService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.clients = clients.stream().collect(
            Collectors.toUnmodifiableMap(OAuthApiClient::oAuthSnsType, Function.identity())
        );
    }

    public LoginResponse normalLogin(LoginServiceRequest loginServiceRequest) throws JsonProcessingException {
        User user = userReadService.findByEmail(loginServiceRequest.getEmail());     //1. 회원조회

        user.checkSnsType(SnsType.NORMAL);                                     //SNS가입여부확인

        if (!bCryptPasswordEncoder.matches(loginServiceRequest.getPassword(), user.getPassword())) {
            throw new LuckKidsException(ErrorCode.USER_PASSWORD);
        } //3. 비밀번호 체크

        return LoginResponse.of(user, setJwtTokenPushKey(user, loginServiceRequest.getDeviceId(), loginServiceRequest.getPushKey()));
    }

    public OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws JsonProcessingException {
        SnsType snsType = oAuthLoginServiceRequest.getSnsType();

        OAuthApiClient client = clients.get(snsType);
        Optional.ofNullable(client).orElseThrow(() -> new LuckKidsException(ErrorCode.OAUTH_UNKNOWN));

        String email = client.getEmail(oAuthLoginServiceRequest.getToken());

        User user = userRepository.findByEmail(email)
            .orElseGet(() -> userRepository.save(
                User.builder()
                    .email(email)
                    .missionCount(0)
                    .snsType(snsType)
                    .role(Role.USER)
                    .settingStatus(SettingStatus.INCOMPLETE)
                    .build()
            ));

        user.checkSnsType(snsType);              //SNS가입여부확인

        return OAuthLoginResponse.of(user, setJwtTokenPushKey(user, oAuthLoginServiceRequest.getDeviceId(), oAuthLoginServiceRequest.getPushKey()));
    }

    private JwtToken setJwtTokenPushKey(User user, String deviceId, String pushKey) throws JsonProcessingException {
        LoginUserInfo userInfo = LoginUserInfo.of(user.getId());
        JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);                   //JWT토큰생성
        user.checkRefreshToken(jwtToken, deviceId);                                 //deviceId로 기존 refreshToken 조회 후 수정 혹은 등록
        user.checkPushKey(pushKey, deviceId);                                       //deviceId로 기존 push 조회 후 수정 혹은 등록
        return jwtToken;
    }

    public LoginGenerateTokenResponse refreshJwtToken(LoginGenerateTokenServiceRequest loginGenerateTokenServiceRequest) {
        User user = userReadService.findByEmail(loginGenerateTokenServiceRequest.getEmail());
        RefreshToken refreshToken = refreshTokenRepository.findByUserIdAndDeviceIdAndRefreshToken(user.getId(), loginGenerateTokenServiceRequest.getDeviceId(), loginGenerateTokenServiceRequest.getRefreshToken())
            .orElseThrow(() -> new LuckKidsException(ErrorCode.JWT_NOT_EXIST));
        JwtToken jwtToken = jwtTokenGenerator.generateJwtToken(refreshToken.getRefreshToken());
        refreshToken.updateRefreshToken(jwtToken.getRefreshToken());
        return LoginGenerateTokenResponse.of(jwtToken);
    }
}
