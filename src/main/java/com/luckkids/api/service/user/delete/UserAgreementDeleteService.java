package com.luckkids.api.service.user.delete;

import com.luckkids.domain.userAgreement.UserAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAgreementDeleteService {

    private final UserAgreementRepository userAgreementRepository;

    public void deleteAllByUserId(int userId) {
        userAgreementRepository.deleteAllByUserId(userId);
    }
}
