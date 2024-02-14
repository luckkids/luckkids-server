package com.luckkids.api.controller.friend.request;

import com.luckkids.api.service.friendCode.request.FriendCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendCreateRequest {

    @NotNull(message = "친구코드는 필수입니다.")
    private String code;

    @Builder
    private FriendCreateRequest(String code) {
        this.code = code;
    }

    public FriendCreateServiceRequest toServiceRequest(){
        return FriendCreateServiceRequest.builder()
            .code(code)
            .build();
    }
}
