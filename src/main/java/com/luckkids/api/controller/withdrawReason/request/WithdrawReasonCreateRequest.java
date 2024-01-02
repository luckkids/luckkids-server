package com.luckkids.api.controller.withdrawReason.request;

import com.luckkids.api.service.withdrawReason.request.WithdrawReasonCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawReasonCreateRequest {

    @NotNull(message = "탈퇴사유는 필수입니다.")
    private String reason;

    @Builder
    private WithdrawReasonCreateRequest(String reason) {
        this.reason = reason;
    }

    public WithdrawReasonCreateServiceRequest toServiceRequest(){
        return WithdrawReasonCreateServiceRequest.builder()
            .reason(reason)
            .build();
    }
}
