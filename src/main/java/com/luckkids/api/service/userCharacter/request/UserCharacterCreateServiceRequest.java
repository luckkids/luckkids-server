package com.luckkids.api.service.userCharacter.request;

import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.CharacterProgressStatus;
import com.luckkids.domain.userCharacter.UserCharacter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterCreateServiceRequest {

    private int id;
    private String nickName;

    @Builder
    private UserCharacterCreateServiceRequest(int id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public UserCharacter toEntity(User user, LuckkidsCharacter luckkidsCharacter) {
        return UserCharacter.builder()
            .user(user)
            .luckkidsCharacter(luckkidsCharacter)
            .characterProgressStatus(CharacterProgressStatus.IN_PROGRESS)
            .build();
    }
}
