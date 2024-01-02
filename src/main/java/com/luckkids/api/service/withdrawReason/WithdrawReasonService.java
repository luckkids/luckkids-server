package com.luckkids.api.service.withdrawReason;

import com.luckkids.api.service.withdrawReason.request.WithdrawReasonCreateServiceRequest;
import com.luckkids.api.service.withdrawReason.response.WithdrawReasonCreateResponse;
import com.luckkids.domain.withdrawReason.WithdrawReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WithdrawReasonService {

    private final WithdrawReasonRepository withdrawReasonRepository;

    public WithdrawReasonCreateResponse create(WithdrawReasonCreateServiceRequest withdrawReasonCreateServiceRequest){
        return WithdrawReasonCreateResponse.of(withdrawReasonRepository.save(withdrawReasonCreateServiceRequest.toEntity()));
    }
}
