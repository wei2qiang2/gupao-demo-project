package com.demo.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

public class IndexController {

    @GetMapping({"/", ""})
    public String index(){
        return "index";
    }

    @ModelAttribute(name = "message")
    public String message(){
        return "Hello World!";
    }
}
