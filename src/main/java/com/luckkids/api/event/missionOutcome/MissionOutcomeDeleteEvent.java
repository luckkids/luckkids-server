package com.luckkids.api.event.missionOutcome;

import org.springframework.context.ApplicationEvent;

public class MissionOutcomeDeleteEvent extends ApplicationEvent {

    private int missionId;

    public MissionOutcomeDeleteEvent(Object source, int missionId) {
        super(source);
        this.missionId = missionId;
    }

    public int getMissionId() {
        return missionId;
    }
}

