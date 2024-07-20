package com.luckkids.api.service.friendCode.response;

import com.luckkids.domain.friendCode.FriendStatus;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendCodeNickNameResponse {

    private String nickName;
    private FriendStatus status;

    @Builder
    private FriendCodeNickNameResponse(String nickName, FriendStatus status) {
        this.nickName = nickName;
        this.status = status;
    }

    public static FriendCodeNickNameResponse of(String nickName, FriendStatus status){
        return FriendCodeNickNameResponse.builder()
                .nickName(nickName)
                .status(status)
                .build();
    }
}