package com.luckkids.notification.service.request;

import com.luckkids.api.service.firebase.request.SendFirebaseServiceRequest;
import com.luckkids.notification.domain.push.Push;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SendPushServiceRequest {

    private Push push;
    private String body;
    private SendPushDataDto sendPushDataDto;

    @Builder
    public SendPushServiceRequest(Push push, String body, SendPushDataDto sendPushDataDto) {
        this.push = push;
        this.body = body;
        this.sendPushDataDto = sendPushDataDto;
    }

    public SendFirebaseServiceRequest toSendPushServiceRequest(){
        return SendFirebaseServiceRequest.builder()
                .body(body)
                .push(push)
                .sendFirebaseDataDto(sendPushDataDto.toFirebaseDataDto())
                .build();
    }
}
