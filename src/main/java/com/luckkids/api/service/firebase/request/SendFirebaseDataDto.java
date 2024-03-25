package com.luckkids.api.service.firebase.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendFirebaseDataDto {

    private String screenName;
    private String url;

    @Builder
    private SendFirebaseDataDto(String screenName, String url) {
        this.screenName = screenName;
        this.url = url;
    }
}
