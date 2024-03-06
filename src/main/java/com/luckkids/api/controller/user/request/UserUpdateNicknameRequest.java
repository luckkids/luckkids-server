package com.luckkids.api.controller.user.request;

import com.luckkids.api.service.user.request.UserUpdateNicknameServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateNicknameRequest {

    @NotBlank(message = "변경 할 닉네임은 필수입니다.")
    private String nickname;

    @Builder
    private UserUpdateNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public UserUpdateNicknameServiceRequest toServiceRequest() {
        return UserUpdateNicknameServiceRequest.builder()
            .nickname(nickname)
            .build();
    }
}
