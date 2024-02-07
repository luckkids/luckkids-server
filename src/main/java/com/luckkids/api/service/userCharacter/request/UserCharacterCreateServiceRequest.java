package com.luckkids.api.service.userCharacter.request;

import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterCreateServiceRequest {

    private String characterNickname;
    private String fileName;

    @Builder
    private UserCharacterCreateServiceRequest(String characterNickname, String fileName) {
        this.characterNickname = characterNickname;
        this.fileName = fileName;
    }

    public UserCharacter toEntity(User user) {
        return UserCharacter.builder()
//            .user(user)   ⭐️
//            .file(fileName)
//            .characterNickname(characterNickname)
//            .level(1)
            .build();
    }
}
