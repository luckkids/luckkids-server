package com.luckkids.api.event.user;

import static java.time.LocalDate.*;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.luckkids.api.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventListener implements ApplicationListener<ApplicationEvent> {

	private final UserService userService;

	@Override
	@TransactionalEventListener
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof UserMissionCountUpdateEvent updateEvent) {
			handleUserMissionCountUpdateEvent(updateEvent.getMissionId());
		}

	}

	private void handleUserMissionCountUpdateEvent(int missionId) {
		userService.minusMissionCount(missionId, now());
	}
}


