package com.luckkids.api.event.user;

import com.luckkids.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventListener implements ApplicationListener<ApplicationEvent> {

    private final UserService userService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserMissionCountUpdateEvent userMissionCountUpdateEvent) {
            handleUserMissionCountUpdateEvent(userMissionCountUpdateEvent);
        }
    }

    private void handleUserMissionCountUpdateEvent(UserMissionCountUpdateEvent event) {
        userService.updateMissionCount(event.getMissionStatus());
    }
}


