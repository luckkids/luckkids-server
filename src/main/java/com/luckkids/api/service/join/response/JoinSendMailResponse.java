package com.luckkids.api.service.join.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinSendMailResponse {

    private String authNum;

    public static JoinSendMailResponse of(String authNum){
        return JoinSendMailResponse.builder()
            .authNum(authNum)
            .build();
    }

}
