package com.luckkids.api.controller.push;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.push.request.PushSoundChangeRequest;
import com.luckkids.api.service.push.PushService;
import com.luckkids.api.service.push.response.PushSoundChangeResponse;
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
