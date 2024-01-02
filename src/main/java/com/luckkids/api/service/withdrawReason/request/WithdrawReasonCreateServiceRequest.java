package com.luckkids.api.service.withdrawReason.request;

import com.luckkids.domain.withdrawReason.WithdrawReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawReasonCreateServiceRequest {

    private String reason;

    @Builder
    private WithdrawReasonCreateServiceRequest(String reason) {
        this.reason = reason;
    }

    public WithdrawReason toEntity(){
        return WithdrawReason.builder()
            .reason(reason)
            .build();
    }
}
