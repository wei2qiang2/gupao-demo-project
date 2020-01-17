package com.example.springapplication.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotationDemo {

    public static void main(String[] args) {


        //找BeanDefinition
        // @Bean @Configuration
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        //注册配置类
        context.register(AnnotationDemo.class);
        //启动上下文
        context.refresh();
        System.out.println(context.getBean(AnnotationDemo.class));
    }
}
