package com.luckkids.api.service.mission;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.event.missionOutcome.MissionOutcomeCreateEvent;
import com.luckkids.api.event.missionOutcome.MissionOutcomeDeleteEvent;
import com.luckkids.domain.misson.Mission;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionEventService {

	private final ApplicationEventPublisher eventPublisher;

	public void publishMissionOutcomeCreateEvent(Mission mission) {
		eventPublisher.publishEvent(new MissionOutcomeCreateEvent(this, mission));
	}

	public void publishMissionOutcomeDeleteEvent(Mission mission) {
		eventPublisher.publishEvent(new MissionOutcomeDeleteEvent(this, mission.getId()));
	}

}
