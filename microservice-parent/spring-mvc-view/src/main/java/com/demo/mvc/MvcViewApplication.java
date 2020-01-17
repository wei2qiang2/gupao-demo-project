package com.demo.mvc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @ClassName com.demo.mvc.MvcViewApplication
 * @Description TODO
 * @Author wq
 * @Date 2019/6/24 9:13
 * @Version 1.0.0
 */
@SpringBootApplication
public class MvcViewApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(MvcViewApplication.class)
                .properties("server.port=50201")
                .run(args);
    }
}
