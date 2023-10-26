package com.luckkids.api.event.missionOutcome;

import com.luckkids.domain.misson.Mission;
import org.springframework.context.ApplicationEvent;

public class MissionOutcomeCreateEvent extends ApplicationEvent {

    private Mission mission;

    public MissionOutcomeCreateEvent(Object source, Mission mission) {
        super(source);
        this.mission = mission;
    }

    public Mission getMission() {
        return mission;
    }
}

