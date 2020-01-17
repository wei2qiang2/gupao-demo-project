package com.demo.event.service;

import com.demo.event.entity.User;
import com.demo.event.event.UserLoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {

    @Autowired
    ApplicationContext context;

    public void login(User user){

        System.out.println("发布事件......");

        context.publishEvent(new UserLoginEvent(this, user));
    }
}
