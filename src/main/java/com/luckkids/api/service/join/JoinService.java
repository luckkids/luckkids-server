package com.luckkids.api.service.join;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userAgreement.UserAgreement;
import com.luckkids.domain.userAgreement.UserAgreementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final UserRepository userRepository;
    private final UserAgreementRepository userAgreementRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JoinReadService joinReadService;

    public JoinResponse joinUser(JoinServiceRequest joinServiceRequest) {
        joinReadService.checkEmail(JoinCheckEmailServiceRequest.builder()
                .email(joinServiceRequest.getEmail())
                .build());

        User user = joinServiceRequest.toEntity();
        user.updatePassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        UserAgreement userAgreement = userAgreementRepository.save(joinServiceRequest.toUserAgreementEntity(savedUser));
        return JoinResponse.of(savedUser, userAgreement);
    }
}
