package com.luckkids.api.service.missionOutcome.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeUpdateResponse {

    private boolean levelUpResult;
    private String lottieFile;
    private String imageFile;

    @Builder
    public MissionOutcomeUpdateResponse(boolean levelUpResult, String lottieFile, String imageFile) {
        this.levelUpResult = levelUpResult;
        this.lottieFile = lottieFile;
        this.imageFile = imageFile;
    }

    public static MissionOutcomeUpdateResponse of(boolean levelUpResult, String lottieFile, String imageFile) {
        return MissionOutcomeUpdateResponse.builder()
            .levelUpResult(levelUpResult)
            .lottieFile(lottieFile)
            .imageFile(imageFile)
            .build();
    }
}
