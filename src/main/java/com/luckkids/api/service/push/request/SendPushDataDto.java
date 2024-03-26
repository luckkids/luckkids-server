package com.luckkids.api.service.push.request;

import com.luckkids.api.service.firebase.request.SendFirebaseDataDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendPushDataDto {

    private String screenName;
    private String url;

    @Builder
    private SendPushDataDto(String screenName, String url) {
        this.screenName = screenName;
        this.url = url;
    }

    public SendFirebaseDataDto toFirebaseDataDto(){
        return SendFirebaseDataDto.builder()
                .screenName(screenName)
                .url(url)
                .build();
    }

}
