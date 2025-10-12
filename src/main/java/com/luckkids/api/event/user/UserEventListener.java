package com.luckkids.api.event.user;

import static java.time.LocalDate.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.luckkids.api.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventListener {

	private final UserService userService;

	@TransactionalEventListener
	public void handleUserMissionCountUpdateEvent(UserMissionCountUpdateEvent event) {
		userService.minusMissionCount(event.getMissionId(), now());
	}

}


