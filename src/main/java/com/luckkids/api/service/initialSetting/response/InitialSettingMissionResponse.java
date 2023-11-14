package com.luckkids.api.service.initialSetting.response;

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
public class InitialSettingMissionResponse {
    private String missionDescription;
    private AlertStatus alertStatus;
    private LocalTime alertTime;

    @Builder
    private InitialSettingMissionResponse(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }
}
