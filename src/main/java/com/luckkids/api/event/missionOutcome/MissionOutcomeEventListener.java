package com.luckkids.api.event.missionOutcome;

import static java.time.LocalDate.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MissionOutcomeEventListener {

	private final MissionOutcomeService missionOutcomeService;

	@TransactionalEventListener
	public void handleCreateEvent(MissionOutcomeCreateEvent event) {
		MissionOutcomeCreateServiceRequest request = MissionOutcomeCreateServiceRequest.builder()
			.mission(event.getMission())
			.missionDate(now())
			.build();

		missionOutcomeService.createMissionOutcome(request);
	}

	@TransactionalEventListener
	public void handleDeleteEvent(MissionOutcomeDeleteEvent event) {
		missionOutcomeService.deleteMissionOutcome(event.getMissionId(), now());
	}
}


