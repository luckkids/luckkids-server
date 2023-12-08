package com.luckkids.api.event.confirmEmail;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConfirmEmaiRemoveEvent extends ApplicationEvent {

    private int id;

    public ConfirmEmaiRemoveEvent(Object source, int id) {
        super(source);
        this.id = id;
    }

}
