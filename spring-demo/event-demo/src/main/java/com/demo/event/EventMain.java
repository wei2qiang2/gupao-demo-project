package com.demo.event;

import com.demo.event.configuration.EventConfiguration;
import com.demo.event.entity.User;
import com.demo.event.service.UserLoginService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EventMain {

    public static void main(String[] args) {
        User user = new User();

        user.setId(1L);
        user.setUserName("魏强");
        user.setPhone("18301313886");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EventConfiguration.class);

        UserLoginService loginService = context.getBean(UserLoginService.class);

        loginService.login(user);

//        context.close();
    }
}
