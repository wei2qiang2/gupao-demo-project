package com.demo.wq.aop.service;

import org.springframework.stereotype.Service;

@Service
public class MethodRuleService {

    public void del(String request, String response){
        System.out.println("request:"+ request);
        System.out.println("response:"+ response);
    }
}
