package com.demo.event.event;

import com.demo.event.entity.User;
import org.springframework.context.ApplicationEvent;

public class UserLoginEvent extends ApplicationEvent {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public UserLoginEvent(Object source, User user) {
        super(source);
        this.user = user;
        System.out.println("create UserLoginEvent...");
    }
}
