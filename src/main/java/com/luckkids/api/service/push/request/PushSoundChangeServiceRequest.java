package com.luckkids.api.service.push.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PushSoundChangeServiceRequest {

    private String deviceId;
    private String sound;

    @Builder
    private PushSoundChangeServiceRequest(String deviceId, String sound) {
        this.deviceId = deviceId;
        this.sound = sound;
    }
}
