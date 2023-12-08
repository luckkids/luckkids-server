package com.luckkids.api.event.confirmEmail;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConfirmEmaiRemoveEvent extends ApplicationEvent {
    private String email;
    private String authKey;

    public ConfirmEmaiRemoveEvent(Object source, String email, String authKey) {
        super(source);
        this.email = email;
        this.authKey = authKey;
    }

}
