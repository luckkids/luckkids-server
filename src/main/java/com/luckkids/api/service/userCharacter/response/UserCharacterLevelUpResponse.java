package com.luckkids.api.service.userCharacter.response;

import com.luckkids.api.service.missionOutcome.response.MissionOutcomeUpdateResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCharacterLevelUpResponse {

    private boolean levelUpResult;
    private String lottieFile;
    private String imageFile;

    @Builder
    private UserCharacterLevelUpResponse(boolean levelUpResult, String lottieFile, String imageFile) {
        this.levelUpResult = levelUpResult;
        this.lottieFile = lottieFile;
        this.imageFile = imageFile;
    }

    public static UserCharacterLevelUpResponse of(boolean levelUpResult, String lottieFile, String imageFile) {
        return UserCharacterLevelUpResponse.builder()
            .levelUpResult(levelUpResult)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();
    }

    public MissionOutcomeUpdateResponse toMissionOutcomeUpdateResponse() {
        return MissionOutcomeUpdateResponse.builder()
            .levelUpResult(this.levelUpResult)
            .lottieFile(this.lottieFile)
            .imageFile(this.imageFile)
            .build();
    }
}
