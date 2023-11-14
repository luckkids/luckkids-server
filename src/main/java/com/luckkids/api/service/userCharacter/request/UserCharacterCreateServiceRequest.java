package com.luckkids.api.service.userCharacter.request;

import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterCreateServiceRequest {

    private String fileName;
    private String characterName;

    @Builder
    private UserCharacterCreateServiceRequest(String fileName, String characterName) {
        this.fileName = fileName;
        this.characterName = characterName;
    }

    public UserCharacter toEntity(User user){
        return UserCharacter.builder()
            .user(user)
            .fileName(fileName)
            .characterName(characterName)
            .level(1)
            .build();
    }
}
