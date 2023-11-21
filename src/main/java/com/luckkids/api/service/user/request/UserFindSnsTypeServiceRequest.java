package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserFindSnsTypeServiceRequest {

    private String email;

    @Builder
    private UserFindSnsTypeServiceRequest(String email) {
        this.email = email;
    }
}
