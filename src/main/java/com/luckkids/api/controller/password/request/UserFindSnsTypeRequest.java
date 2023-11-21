package com.luckkids.api.controller.password.request;

import com.luckkids.api.service.user.request.UserFindSnsTypeServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserFindSnsTypeRequest {

    private String email;

    @Builder
    private UserFindSnsTypeRequest(String email) {
        this.email = email;
    }

    public UserFindSnsTypeServiceRequest toServiceRequest(){
        return UserFindSnsTypeServiceRequest.builder()
            .email(email)
            .build();
    }
}
