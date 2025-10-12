package com.luckkids.api.event.missionOutcome;

import static java.time.LocalDate.*;
import static org.springframework.transaction.annotation.Propagation.*;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MissionOutcomeEventListener {

	private final MissionOutcomeService missionOutcomeService;

	@Transactional(propagation = MANDATORY)
	@EventListener
	public void handleCreateEvent(MissionOutcomeCreateEvent event) {
		MissionOutcomeCreateServiceRequest request = MissionOutcomeCreateServiceRequest.builder()
			.mission(event.getMission())
			.missionDate(now())
			.build();

		missionOutcomeService.createMissionOutcome(request);
	}

	@Transactional(propagation = MANDATORY)
	@EventListener
	public void handleDeleteEvent(MissionOutcomeDeleteEvent event) {
		missionOutcomeService.deleteMissionOutcome(event.getMissionId(), now());
	}
}


