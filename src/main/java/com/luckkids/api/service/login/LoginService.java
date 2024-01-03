package com.luckkids.api.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.login.request.LoginGenerateTokenServiceRequest;
import com.luckkids.api.service.login.request.LoginServiceRequest;
import com.luckkids.api.service.login.response.LoginGenerateTokenResponse;
import com.luckkids.api.service.login.response.LoginResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.refreshToken.RefreshToken;
import com.luckkids.domain.refreshToken.RefreshTokenRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.jwt.JwtTokenGenerator;
import com.luckkids.jwt.dto.JwtToken;
import com.luckkids.jwt.dto.UserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserReadService userReadService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse normalLogin(LoginServiceRequest loginServiceRequest) throws JsonProcessingException {
        User user = userReadService.findByEmail(loginServiceRequest.getEmail());     //1. 회원조회

        Optional.ofNullable(user).orElseThrow(()->new LuckKidsException(ErrorCode.USER_UNKNOWN));

        user.loginCheckSnsType(SnsType.NORMAL);                                     //2. SNS가입여부확인

        if(!bCryptPasswordEncoder.matches(loginServiceRequest.getPassword(), user.getPassword())){
            throw new LuckKidsException(ErrorCode.USER_PASSWORD);
        } //3. 비밀번호 체크

        UserInfo userInfo = UserInfo.of(user.getId(), user.getEmail());
        JwtToken jwtToken = jwtTokenGenerator.generate(userInfo);                   //4. JWT토큰생성

        String deviceId = loginServiceRequest.getDeviceId();                        //5. deviceId로 기존 refreshToken 조회 후 수정 혹은 등록
        user.checkRefreshToken(jwtToken, deviceId);

        String pushKey = loginServiceRequest.getPushKey();                          //6. deviceId로 기존 push 조회 후 수정 혹은 등록
        user.checkPushKey(pushKey, deviceId);

        return LoginResponse.of(user, jwtToken);
    }

    public LoginGenerateTokenResponse generateAccessToken(LoginGenerateTokenServiceRequest loginGenerateTokenServiceRequest){
        User user = userReadService.findByEmail(loginGenerateTokenServiceRequest.getEmail());
        RefreshToken refreshToken = refreshTokenRepository.findByUserIdAndDeviceIdAndRefreshToken(user.getId(), loginGenerateTokenServiceRequest.getDeviceId(), loginGenerateTokenServiceRequest.getRefreshToken())
            .orElseThrow(() -> new LuckKidsException(ErrorCode.JWT_NOT_EXIST));
        return LoginGenerateTokenResponse.of(jwtTokenGenerator.generateAccessToken(refreshToken.getRefreshToken()));
    }
}
