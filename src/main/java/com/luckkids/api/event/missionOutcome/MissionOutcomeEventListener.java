package com.luckkids.api.event.missionOutcome;

import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static java.time.LocalDate.now;

@Component
@RequiredArgsConstructor
public class MissionOutcomeEventListener implements ApplicationListener<MissionOutcomeCreateEvent> {

    private final MissionOutcomeService missionOutcomeService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(MissionOutcomeCreateEvent event) {
        MissionOutcomeCreateServiceRequest request = MissionOutcomeCreateServiceRequest.builder()
            .mission(event.getMission())
            .missionDate(now())
            .build();

        missionOutcomeService.createMissionOutcome(request);
    }
}


