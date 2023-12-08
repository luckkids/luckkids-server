package com.luckkids.api.event.confirmEmail;

import com.luckkids.api.service.confirmEmail.ConfirmEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmEmailEventListener implements ApplicationListener<ApplicationEvent> {

    private final ConfirmEmailService confirmEmailService;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ConfirmEmaiRemoveEvent removeEvent) {
            handleRemoveEvent(removeEvent);
        }
    }

    @Async
    public void handleRemoveEvent(ConfirmEmaiRemoveEvent event) {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    confirmEmailService.removeConfirmEmail(event.getId());
                    timer.cancel();
                }
            }, 3 * 60 * 1000);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
