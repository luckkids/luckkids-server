package com.luckkids.api.service.push.response;

import com.luckkids.domain.push.Push;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PushSoundChangeResponse {

    private String deviceId;

    private String pushToken;

    private String sound;

    @Builder
    private PushSoundChangeResponse(String deviceId, String pushToken, String sound) {
        this.deviceId = deviceId;
        this.pushToken = pushToken;
        this.sound = sound;
    }

    public static PushSoundChangeResponse of(Push push){
        return PushSoundChangeResponse.builder()
            .deviceId(push.getDeviceId())
            .pushToken(push.getPushToken())
            .sound(push.getSound())
            .build();
    }

}
