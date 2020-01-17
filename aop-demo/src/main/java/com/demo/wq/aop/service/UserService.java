package com.demo.wq.aop.service;

import com.demo.wq.aop.annotation.Action;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Action(action = "添加用户")
    public void add(){
        System.out.println("user add...");
//        return true;
    }
}
