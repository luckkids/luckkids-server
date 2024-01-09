package com.luckkids.api.service.friend.response;

import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendCreateResponse {

    private String requester;
    private String receiver;

    @Builder
    private FriendCreateResponse(String requester, String receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }

    public static FriendCreateResponse of(User request, User receive){
        return FriendCreateResponse.builder()
            .requester(request.getEmail())
            .receiver(receive.getEmail())
            .build();
    }
}
