package com.luckkids.api.service.withdrawReason.response;

import com.luckkids.domain.withdrawReason.WithdrawReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawReasonCreateResponse {

    private int id;

    @Builder
    private WithdrawReasonCreateResponse(int id) {
        this.id = id;
    }

    public static WithdrawReasonCreateResponse of(WithdrawReason withdrawReason) {
        return WithdrawReasonCreateResponse.builder()
            .id(withdrawReason.getId())
            .build();
    }
}
