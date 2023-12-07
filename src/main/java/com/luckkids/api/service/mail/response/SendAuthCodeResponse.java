package com.luckkids.api.service.mail.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendAuthCodeResponse {

    private String authNum;

    @Builder
    private SendAuthCodeResponse(String authNum) {
        this.authNum = authNum;
    }

    public static SendAuthCodeResponse of(String authNum){
        return SendAuthCodeResponse.builder()
            .authNum(authNum)
            .build();
    }

}
