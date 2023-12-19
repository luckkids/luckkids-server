package com.luckkids.api.service.mail.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendAuthUrlResponse {

    private String authKey;

    @Builder
    private SendAuthUrlResponse(String authKey) {
        this.authKey = authKey;
    }

    public static SendAuthUrlResponse of(String authKey){
        return SendAuthUrlResponse.builder()
            .authKey(authKey)
            .build();
    }

}
