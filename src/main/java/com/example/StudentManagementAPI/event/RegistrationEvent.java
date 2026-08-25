package com.example.StudentManagementAPI.event;

import com.example.StudentManagementAPI.Entity.User;
import org.springframework.context.ApplicationEvent;

public class RegistrationEvent extends ApplicationEvent {
    private final User user;

    public RegistrationEvent( User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
