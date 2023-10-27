package com.luckkids.api.controller.missionOutcome.request;

import com.luckkids.domain.missionOutcome.MissionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionOutcomeUpdateRequest {

    @NotNull(message = "미션 성공 여부는 필수입니다.")
    private MissionStatus missionStatus;

    @Builder
    private MissionOutcomeUpdateRequest(MissionStatus missionStatus) {
        this.missionStatus = missionStatus;
    }
}