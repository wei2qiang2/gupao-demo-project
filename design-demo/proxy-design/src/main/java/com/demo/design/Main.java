package com.demo.design;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
                .properties("server.port=9090")
                .bannerMode(Banner.Mode.OFF)
                .run(args);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String baenName:beanDefinitionNames) {
            System.out.println("beanName:" + baenName);
        }
    }
}
