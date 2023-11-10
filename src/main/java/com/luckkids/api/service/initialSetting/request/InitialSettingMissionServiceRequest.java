package com.luckkids.api.service.initialSetting.request;

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
public class InitialSettingMissionServiceRequest {
    @NotBlank(message = "미션 내용은 필수입니다.")
    private String missionDescription;

    @NotNull(message = "알람 여부는 필수입니다.")
    private AlertStatus alertStatus;

    @NotNull(message = "알람 시간은 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime alertTime;

    @Builder
    private InitialSettingMissionServiceRequest(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }
}
