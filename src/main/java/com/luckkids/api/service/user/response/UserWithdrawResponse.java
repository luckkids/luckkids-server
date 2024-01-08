package com.luckkids.api.service.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserWithdrawResponse {
    private int id;

    @Builder
    private UserWithdrawResponse(int id) {
        this.id = id;
    }

    public static UserWithdrawResponse of(int id){
        return UserWithdrawResponse.builder()
            .id(id)
            .build();
    }
}
