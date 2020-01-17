package com.example.springapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }

    /**
     * 启动方式一
     *
     * @param args
     */
    public static void test1(String[] args) {
        new SpringApplicationBuilder(Application.class)
                //单元测试的时候 port = RANDOM
                .properties("server.port=0") //随机选择一个可用的端口
                .run(args);
    }

    /**
     * 启动方式二
     *
     * @param args
     */
    public static void test2(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("server.port", 0);
        application.setDefaultProperties(map);

        application.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext context
                = application.run(args);
        System.out.println(context);
    }

}
