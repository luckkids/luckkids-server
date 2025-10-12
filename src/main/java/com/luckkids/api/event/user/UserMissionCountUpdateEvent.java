package com.luckkids.api.event.user;

import org.springframework.context.ApplicationEvent;

public class UserMissionCountUpdateEvent extends ApplicationEvent {

	private int missionId;

	public UserMissionCountUpdateEvent(Object source, int missionId) {
		super(source);
		this.missionId = missionId;
	}

	public int getMissionId() {
		return missionId;
	}
}
