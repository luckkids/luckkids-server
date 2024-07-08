package com.luckkids.api.controller.friendCode.request;

import com.luckkids.api.service.friendCode.request.FriendCodeNickNameServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendCodeNickNameRequest {

    @NotNull(message = "친구코드는 필수입니다.")
    private String code;

    @Builder
    private FriendCodeNickNameRequest(String code) {
        this.code = code;
    }

    public FriendCodeNickNameServiceRequest toServiceRequest() {
        return FriendCodeNickNameServiceRequest.builder()
                .code(code)
                .build();
    }
}
