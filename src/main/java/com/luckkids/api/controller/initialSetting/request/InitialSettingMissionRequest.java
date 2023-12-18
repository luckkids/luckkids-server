package com.luckkids.api.controller.initialSetting.request;

import com.luckkids.api.service.initialSetting.request.InitialSettingMissionServiceRequest;
import com.luckkids.domain.misson.AlertStatus;
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
    @NotBlank(message = "미션 내용은 필수입니다.")
    private String missionDescription;

    @NotNull(message = "알람 시간은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime alertTime;

    @Builder
    private InitialSettingMissionRequest(String missionDescription, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
    }

    public InitialSettingMissionServiceRequest toServiceRequest(){
        return InitialSettingMissionServiceRequest.builder()
            .missionDescription(missionDescription)
            .alertTime(alertTime)
            .build();
    }
}
