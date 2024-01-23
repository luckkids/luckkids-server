package com.luckkids.api.service.withdrawReason;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.withdrawReason.request.WithdrawReasonCreateServiceRequest;
import com.luckkids.api.service.withdrawReason.response.WithdrawReasonCreateResponse;
import com.luckkids.domain.withdrawReason.WithdrawReason;
import com.luckkids.domain.withdrawReason.WithdrawReasonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class WithdrawReasonServiceTest extends IntegrationTestSupport {

    @Autowired
    private WithdrawReasonRepository withdrawReasonRepository;

    @Autowired
    private WithdrawReasonService withdrawReasonService;

    @Test
    @DisplayName("탈퇴사유를 저장한다.")
    void withdrawReasonTest() {
        WithdrawReasonCreateServiceRequest withdrawReasonCreateServiceRequest = WithdrawReasonCreateServiceRequest.builder()
            .reason("잘 사용하지 않는 앱이예요.")
            .build();

        WithdrawReasonCreateResponse withdrawReasonCreateResponse = withdrawReasonService.create(withdrawReasonCreateServiceRequest);

        WithdrawReason savedWithdrawReason = withdrawReasonRepository.findById((long) withdrawReasonCreateResponse.getId()).get();

        assertThat(savedWithdrawReason.getReason()).isEqualTo("잘 사용하지 않는 앱이예요.");

    }
}
