package com.luckkids.api.service.join.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinCheckEmailServiceRequest {

    private String email;
}
