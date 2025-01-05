package com.luckkids.api.service.join;

import com.luckkids.api.service.alertHistory.AlertHistoryService;
import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.push.PushMessage;
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
    private final AlertHistoryService alertHistoryService;

    public JoinResponse joinUser(JoinServiceRequest joinServiceRequest) {
        joinReadService.checkEmail(JoinCheckEmailServiceRequest.builder()
                .email(joinServiceRequest.getEmail())
                .build());

        User user = joinServiceRequest.toEntity();
        user.updatePassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        UserAgreement userAgreement = createUserAgreement(joinServiceRequest.toUserAgreementEntity(savedUser));
        alertHistoryService.createWelcomeAlertHistory(savedUser);

        return JoinResponse.of(savedUser, userAgreement);
    }

    private UserAgreement createUserAgreement(UserAgreement userAgreement) {
        return userAgreementRepository.save(userAgreement);
    }
}
