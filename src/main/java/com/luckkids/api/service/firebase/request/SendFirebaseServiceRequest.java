package com.luckkids.api.service.firebase.request;

import com.luckkids.notification.domain.push.Push;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendFirebaseServiceRequest {
    private Push push;
    private String sound;
    private String body;
    private SendFirebaseDataDto sendFirebaseDataDto;

    @Builder
    private SendFirebaseServiceRequest(Push push, String sound, String body, SendFirebaseDataDto sendFirebaseDataDto) {
        this.push = push;
        this.sound = sound;
        this.body = body;
        this.sendFirebaseDataDto = sendFirebaseDataDto;
    }
}
