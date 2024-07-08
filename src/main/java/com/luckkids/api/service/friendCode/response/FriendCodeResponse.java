package com.luckkids.api.service.friendCode.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendCodeResponse {

    private String requester;

    @Builder
    private FriendCodeResponse(String requester) {
        this.requester = requester;
    }

    public static FriendCodeResponse of(User request){
        return FriendCodeResponse.builder()
                .requester(request.getNickname())
                .build();
    }
}