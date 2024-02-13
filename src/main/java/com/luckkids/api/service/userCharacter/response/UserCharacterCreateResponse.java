package com.luckkids.api.service.userCharacter.response;

import com.luckkids.api.service.initialSetting.response.InitialSettingCharacterResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterCreateResponse {

    private int id;
    private String lottieFile;
    private String nickName;

    @Builder
    private UserCharacterCreateResponse(int id, String lottieFile, String nickName) {
        this.id = id;
        this.lottieFile = lottieFile;
        this.nickName = nickName;
    }

    public static UserCharacterCreateResponse of(User user, UserCharacter userCharacter) {
        return UserCharacterCreateResponse.builder()
            .id(userCharacter.getId())
            .lottieFile(userCharacter.getLuckkidsCharacter().getLottieFile())
            .nickName(user.getNickname())
            .build();
    }

    public InitialSettingCharacterResponse toInitialSettingResponse() {
        return InitialSettingCharacterResponse.builder()
            .id(id)
            .lottieFile(lottieFile)
            .nickName(nickName)
            .build();
    }

}
