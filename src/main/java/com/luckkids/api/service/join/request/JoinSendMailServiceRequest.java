package com.luckkids.api.service.join.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinSendMailServiceRequest {

    private String email;
}
