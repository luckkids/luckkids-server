package com.luckkids.api.event.user;

import static java.time.LocalDate.*;
import static org.springframework.transaction.annotation.Propagation.*;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventListener {

	private final UserService userService;

	@Transactional(propagation = MANDATORY)
	@EventListener
	public void handleUserMissionCountUpdateEvent(UserMissionCountUpdateEvent event) {
		userService.minusMissionCount(event.getMissionId(), now());
	}
}
