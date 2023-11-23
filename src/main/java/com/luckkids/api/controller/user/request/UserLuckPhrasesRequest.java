package com.luckkids.api.controller.user.request;

import com.luckkids.api.service.user.request.UserLuckPhrasesServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLuckPhrasesRequest {

    @NotBlank(message = "변경 할 행운문구는 필수입니다.")
    private String luckPhrases;

    @Builder
    private UserLuckPhrasesRequest(String luckPhrases) {
        this.luckPhrases = luckPhrases;
    }

    public UserLuckPhrasesServiceRequest toServiceRequest(){
        return UserLuckPhrasesServiceRequest.builder()
            .luckPhrases(luckPhrases)
            .build();
    }

}
