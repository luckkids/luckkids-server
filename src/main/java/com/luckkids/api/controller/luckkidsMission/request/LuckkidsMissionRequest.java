package com.luckkids.api.controller.luckkidsMission.request;

import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionServiceRequest;
import com.luckkids.domain.misson.MissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckkidsMissionRequest {

    @NotNull(message = "미션타입은 필수입니다.")
    private MissionType missionType;
    @NotNull(message = "미션설명은 필수입니다.")
    private String missionDescription;
    @NotNull(message = "알림시간은 필수입니다.")
    private LocalTime alertTime;

    @Builder
    private LuckkidsMissionRequest(MissionType missionType, String missionDescription, LocalTime alertTime) {
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
    }

    public LuckkidsMissionServiceRequest toServiceRequest(){
        return LuckkidsMissionServiceRequest.builder()
            .missionType(missionType)
            .missionDescription(missionDescription)
            .alertTime(alertTime)
            .build();
    }
}
