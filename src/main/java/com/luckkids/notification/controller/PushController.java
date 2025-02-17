package com.luckkids.notification.controller;

import com.luckkids.api.ApiResponse;
import com.luckkids.notification.controller.request.PushSoundChangeRequest;
import com.luckkids.notification.service.PushService;
import com.luckkids.notification.service.response.PushSoundChangeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/push")
public class PushController {

    private final PushService pushService;

    @PatchMapping
    public ApiResponse<PushSoundChangeResponse> updateSound(@Valid @RequestBody PushSoundChangeRequest pushSoundChangeRequest){
        return ApiResponse.ok(pushService.updateSound(pushSoundChangeRequest.toServiceRequest()));
    }
}
