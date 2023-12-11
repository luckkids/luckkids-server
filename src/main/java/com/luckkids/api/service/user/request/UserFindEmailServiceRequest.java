package com.luckkids.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserFindEmailServiceRequest {

    private String email;

    @Builder
    private UserFindEmailServiceRequest(String email) {
        this.email = email;
    }
}
