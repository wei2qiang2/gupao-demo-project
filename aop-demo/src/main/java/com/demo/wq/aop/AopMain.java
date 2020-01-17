package com.demo.wq.aop;

import com.demo.wq.aop.config.SpringContextConfig;
import com.demo.wq.aop.service.MethodRuleService;
import com.demo.wq.aop.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopMain {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringContextConfig.class);

//        UserService userService = context.getBean(UserService.class);
//        System.out.println(userService.add());

//        userService.add();
        MethodRuleService methodRuleService = context.getBean(MethodRuleService.class);

        methodRuleService.del("request", "response");
        context.close();
    }
}
