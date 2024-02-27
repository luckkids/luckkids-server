package com.luckkids.api.service.userCharacter.response;

import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.userCharacter.projection.UserCharacterSummaryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class UserCharacterSummaryResponse {
    private UserCharacterSummaryDto inProgressCharacter;
    private Map<CharacterType, Long> completedCharacterCount;

    @Builder
    private UserCharacterSummaryResponse(UserCharacterSummaryDto inProgressCharacter, Map<CharacterType, Long> completedCharacterCount) {
        this.inProgressCharacter = inProgressCharacter;
        this.completedCharacterCount = completedCharacterCount;
    }

    public static UserCharacterSummaryResponse of(UserCharacterSummaryDto inProgressCharacter, Map<CharacterType, Long> completedCharacterCount) {
        return UserCharacterSummaryResponse.builder()
            .inProgressCharacter(inProgressCharacter)
            .completedCharacterCount(completedCharacterCount)
            .build();
    }
}
