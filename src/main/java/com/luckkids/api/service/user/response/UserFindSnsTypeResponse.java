package com.luckkids.api.service.user.response;

import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserFindSnsTypeResponse {

    private SnsType snsType;

    @Builder
    private UserFindSnsTypeResponse(SnsType snsType) {
        this.snsType = snsType;
    }


    public static UserFindSnsTypeResponse of(User user){
        return UserFindSnsTypeResponse.builder()
            .snsType(user.getSnsType())
            .build();
    }

}
