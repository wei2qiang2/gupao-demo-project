package com.demo.wq.aop.aspect;

import com.demo.wq.aop.annotation.Action;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 注解拦截
 */
@Aspect
@Component
public class LogAnnotationAspect {

    /**
     * 声明切入点
     */
    @Pointcut("@annotation(com.demo.wq.aop.annotation.Action)")
    public void annotationPointCut(){ }

//    @After(value = "annotationPointCut()")
//    public void after(JoinPoint joinPoint) {
//
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Action annotation = method.getAnnotation(Action.class);
//
//        System.out.println("注解式拦截：" + annotation.action());
//
//    }
//
//    @Before(value = "annotationPointCut()")
//    public void before(JoinPoint joinPoint) {
//
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Action annotation = method.getAnnotation(Action.class);
//
//        System.out.println("注解式拦截：" + annotation.action());
//
//    }

    @Around(value = "annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            System.out.println(123456);
            result = joinPoint.proceed();

            System.out.println(123);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 33;
    }

}
