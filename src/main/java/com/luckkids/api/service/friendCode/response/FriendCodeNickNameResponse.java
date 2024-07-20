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
    private FriendStatus friendStatus;

    @Builder
    private FriendCodeNickNameResponse(String nickName, FriendStatus friendStatus) {
        this.nickName = nickName;
        this.friendStatus = friendStatus;
    }

    public static FriendCodeNickNameResponse of(String nickName, FriendStatus friendStatus){
        return FriendCodeNickNameResponse.builder()
                .nickName(nickName)
                .friendStatus(friendStatus)
                .build();
    }
}