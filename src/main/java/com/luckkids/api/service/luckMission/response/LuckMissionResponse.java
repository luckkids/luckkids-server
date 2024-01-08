package com.luckkids.api.service.luckMission.response;

import com.luckkids.domain.luckMission.LuckMission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class LuckMissionResponse {
    private String description;
    private LocalTime alertTime;

    @Builder
    private LuckMissionResponse( String description, LocalTime alertTime) {
        this.description = description;
        this.alertTime = alertTime;
    }

    public static LuckMissionResponse of(LuckMission luckMission){
        return LuckMissionResponse.builder()
            .description(luckMission.getDescription())
            .alertTime(luckMission.getAlertTime())
            .build();
    }
}
