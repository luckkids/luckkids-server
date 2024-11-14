package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingMissionServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.MissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class InitialSettingMissionRequest {

    private int luckkidsMissionId;
    @NotNull(message = "미션 타입은 필수입니다.")
    private MissionType missionType;
    @NotBlank(message = "미션 내용은 필수입니다.")
    private String missionDescription;
    @NotNull(message = "알람 시간은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime alertTime;

    @Builder
    private InitialSettingMissionRequest(int luckkidsMissionId, MissionType missionType, String missionDescription, LocalTime alertTime) {
        this.luckkidsMissionId = luckkidsMissionId;
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
    }

    public InitialSettingMissionServiceRequest toServiceRequest(){
        return InitialSettingMissionServiceRequest.builder()
            .missionType(missionType)
            .missionDescription(missionDescription)
            .alertTime(alertTime)
            .build();
    }
}
