package com.example.springapplication.spring;

import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

public class SpringEventDemo {

    public static void main(String[] args) {
       ApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

       eventMulticaster.addApplicationListener(applicationEvent -> {
           System.out.println("接收到事件..." + applicationEvent);
       });

       eventMulticaster.multicastEvent(new PayloadApplicationEvent<Object>("", "Hello World!"));
    }
}
