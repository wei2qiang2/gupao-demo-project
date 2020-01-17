package com.demo.event.listener;

import com.demo.event.event.UserLoginEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LoginEventListener implements ApplicationListener<UserLoginEvent> {
    @Override
    public void onApplicationEvent(UserLoginEvent event) {
        System.out.println("event listener is working...");
        System.out.println("监听器：" + event.getUser().getUserName() + "登陆了系统;");
    }
}
