package com.luckkids.api.controller.push.request;

import com.luckkids.api.service.push.request.PushSoundChangeServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PushSoundChangeRequest {

    @NotNull(message = "DeviceId는 필수입니다.")
    private String deviceId;
    private String sound;

    @Builder
    private PushSoundChangeRequest(String deviceId, String sound) {
        this.deviceId = deviceId;
        this.sound = sound;
    }

    public PushSoundChangeServiceRequest toServiceRequest(){
        return PushSoundChangeServiceRequest.builder()
            .deviceId(deviceId)
            .sound(sound)
            .build();
    }
}
