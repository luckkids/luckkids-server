package com.luckkids.api.service.missionOutcome.response;

import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalenderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class MissionOutcomeForCalendarResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<MissionOutcomeCalenderDto> calender;

    @Builder
    private MissionOutcomeForCalendarResponse(LocalDate startDate, LocalDate endDate, List<MissionOutcomeCalenderDto> calender) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.calender = calender;
    }

    public static MissionOutcomeForCalendarResponse of(LocalDate startDate, LocalDate endDate, List<MissionOutcomeCalenderDto> calender) {
        return MissionOutcomeForCalendarResponse.builder()
            .startDate(startDate)
            .endDate(endDate)
            .calender(calender)
            .build();
    }
}
