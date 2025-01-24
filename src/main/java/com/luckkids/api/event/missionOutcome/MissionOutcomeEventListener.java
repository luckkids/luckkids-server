package com.luckkids.api.event.missionOutcome;

import com.luckkids.mission.service.MissionOutcomeService;
import com.luckkids.mission.service.request.MissionOutcomeCreateServiceRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static java.time.LocalDate.now;

@Component
@RequiredArgsConstructor
public class MissionOutcomeEventListener implements ApplicationListener<ApplicationEvent> {

	private final MissionOutcomeService missionOutcomeService;

	@Override
	@TransactionalEventListener
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof MissionOutcomeCreateEvent createEvent) {
			handleCreateEvent(createEvent);
		} else if (event instanceof MissionOutcomeDeleteEvent deleteEvent) {
			handleDeleteEvent(deleteEvent);
		}
	}

	private void handleCreateEvent(MissionOutcomeCreateEvent event) {
		MissionOutcomeCreateServiceRequest request = MissionOutcomeCreateServiceRequest.builder()
			.mission(event.getMission())
			.missionDate(now())
			.build();

		missionOutcomeService.createMissionOutcome(request);
	}

	private void handleDeleteEvent(MissionOutcomeDeleteEvent event) {
		missionOutcomeService.deleteMissionOutcome(event.getMissionId(), now());
	}
}


