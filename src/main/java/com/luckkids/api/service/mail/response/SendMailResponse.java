package com.luckkids.api.service.mail.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendMailResponse {

    private String authNum;

    @Builder
    private SendMailResponse(String authNum) {
        this.authNum = authNum;
    }

    public static SendMailResponse of(String authNum){
        return SendMailResponse.builder()
            .authNum(authNum)
            .build();
    }

}
