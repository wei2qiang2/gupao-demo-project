package com.demo.wq.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class MethodRuleAspect {

//    @Before("execution(* com.demo.wq.aop.service..*.*(..))")
//    public void before(JoinPoint joinPoint){
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//
//        Method method = signature.getMethod();
//
//        System.out.println("方法规则拦截");
//    }

//    @Around(value = "execution(* com.demo.wq.aop.service..*.*(..))", argNames = "request,response")
    @Around(value = "execution(* com.demo.wq.aop.service.MethodRuleService.*(String,String))&&args(request,response)",
            argNames = "joinPoint,response,request")
    public void around(ProceedingJoinPoint joinPoint, String response, String request) throws Throwable {

        System.out.println(request);

        System.out.println(response);

        Object proceed = joinPoint.proceed();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        System.out.println("方法规则拦截");
    }
}
