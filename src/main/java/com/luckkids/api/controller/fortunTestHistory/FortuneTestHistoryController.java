package com.luckkids.api.controller.fortunTestHistory;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.fortunTestHistory.request.FortuneTestHistoryCreateRequest;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryReadService;
import com.luckkids.api.service.fortuneTestHistory.FortuneTestHistoryService;
import com.luckkids.api.service.fortuneTestHistory.response.FortuneTestHistoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fortuneTest")
public class FortuneTestHistoryController {

    private final FortuneTestHistoryService fortuneTestHistoryService;
    private final FortuneTestHistoryReadService fortuneTestHistoryReadService;

    @PostMapping
    public ApiResponse<FortuneTestHistoryResponse> createFortuneTestHistory(
            @Valid @RequestBody FortuneTestHistoryCreateRequest request) {
        return ApiResponse.ok(fortuneTestHistoryService.createFortuneTestHistory(
                request.toServiceRequest()));
    }

    @GetMapping
    public ApiResponse<FortuneTestHistoryResponse> findByUuid(String uuid) {
        return ApiResponse.ok(fortuneTestHistoryReadService.findByUuid(uuid));
    }
}
