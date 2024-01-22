package com.luckkids.api.event.user;

import com.luckkids.domain.missionOutcome.MissionStatus;
import org.springframework.context.ApplicationEvent;

public class UserMissionCountUpdateEvent extends ApplicationEvent {

    private MissionStatus missionStatus;

    public UserMissionCountUpdateEvent(Object source, MissionStatus missionStatus) {
        super(source);
        this.missionStatus = missionStatus;
    }

    public MissionStatus getMissionStatus() {
        return missionStatus;
    }
}
