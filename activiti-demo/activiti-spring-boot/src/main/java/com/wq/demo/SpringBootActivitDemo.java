package com.wq.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootActivitDemo {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootActivitDemo.class);
         ConfigurableApplicationContext run = app.run();
    }
}
