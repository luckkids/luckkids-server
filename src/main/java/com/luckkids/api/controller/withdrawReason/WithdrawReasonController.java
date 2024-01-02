package com.luckkids.api.controller.withdrawReason;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.withdrawReason.request.WithdrawReasonCreateRequest;
import com.luckkids.api.service.withdrawReason.WithdrawReasonService;
import com.luckkids.api.service.withdrawReason.response.WithdrawReasonCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/withdraw")
public class WithdrawReasonController {

    private final WithdrawReasonService withdrawReasonService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reason")
    public ApiResponse<WithdrawReasonCreateResponse> create(@RequestBody @Valid WithdrawReasonCreateRequest withdrawReasonCreateRequest){
        return ApiResponse.created(withdrawReasonService.create(withdrawReasonCreateRequest.toServiceRequest()));
    }
}
