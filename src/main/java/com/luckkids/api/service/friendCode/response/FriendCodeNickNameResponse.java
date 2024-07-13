package com.luckkids.api.service.friendCode.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendCodeNickNameResponse {

    private String nickName;

    @Builder
    private FriendCodeNickNameResponse(String nickName) {
        this.nickName = nickName;
    }

    public static FriendCodeNickNameResponse of(String nickName){
        return FriendCodeNickNameResponse.builder()
                .nickName(nickName)
                .build();
    }
}