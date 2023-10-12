package com.luckkids.api.service.login;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.repository.login.LoginRepository;
import com.luckkids.api.repository.refreshToken.RefreshTokenRepository;
import com.luckkids.api.service.login.dto.LoginServiceRequest;
import com.luckkids.api.service.login.dto.LoginServiceResponse;
import com.luckkids.domain.user.User;
import com.luckkids.security.JwtTokenGenerator;
import com.luckkids.security.dto.JwtToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final LoginRepository loginRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    public LoginServiceResponse login(LoginServiceRequest loginServiceRequest){
        //1. 회원조회
        User user = loginRepository.findByEmail(loginServiceRequest.getEmail())
                .orElseThrow(() -> new LuckKidsException(ErrorCode.USER_UNKNOWN));

        //2. SNS가입여부확인
        user.checkSnsType();

        //3. 비밀번호 체크 -> 암호화는 추후 추가예정
        user.checkPassword(loginServiceRequest.getPassword());

        //4. JWT토큰생성
        JwtToken jwtToken = jwtTokenGenerator.generate(user.getEmail());

        //5. deviceId로 기존 refreshToken 조회 후 수정 혹은 등록
        String deviceId = loginServiceRequest.getDeviceId();
        user.checkRefreshToken(jwtToken, deviceId);

        return new LoginServiceResponse(user, jwtToken);
    }

}
