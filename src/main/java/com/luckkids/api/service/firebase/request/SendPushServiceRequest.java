package com.luckkids.api.service.firebase.request;

import com.luckkids.domain.push.Push;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendPushServiceRequest {
    private Push push;
    private String sound;
    private String title;
    private String body;
    private String screenName;

    @Builder
    private SendPushServiceRequest(Push push, String sound, String title, String body, String screenName) {
        this.push = push;
        this.sound = sound;
        this.title = title;
        this.body = body;
        this.screenName = screenName;
    }
}
