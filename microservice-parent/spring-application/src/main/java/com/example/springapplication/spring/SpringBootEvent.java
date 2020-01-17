package com.example.springapplication.spring;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableAutoConfiguration
public class SpringBootEvent {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootEvent.class)
                .listeners(applicationEvent -> {
                    System.out.println("监听到事件：" + applicationEvent.getClass());
                } )
                .run(args);
    }
}
