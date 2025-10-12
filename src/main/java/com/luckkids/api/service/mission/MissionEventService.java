package com.luckkids.api.service.mission;

import static com.luckkids.domain.misson.MissionActive.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.event.missionOutcome.MissionOutcomeCreateEvent;
import com.luckkids.api.event.missionOutcome.MissionOutcomeDeleteEvent;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionActive;

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

	public void handleMissionStateTransition(Mission mission, MissionActive currentStatus, MissionActive newStatus) {
		if (newStatus == null)
			return;

		switch (currentStatus) {
			case FALSE -> {
				if (newStatus == TRUE) {
					publishMissionOutcomeCreateEvent(mission);
				}
			}
			case TRUE -> {
				if (newStatus == FALSE) {
					publishMissionOutcomeDeleteEvent(mission);
				}
			}
		}
	}
}
