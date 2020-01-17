package com.demo.websocket.controller;

import com.demo.websocket.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {
    @Autowired
    SimpMessagingTemplate template;

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
        Consts.userList.add(username);
        request.getSession().setAttribute("uname",username);
        response.sendRedirect("main.html");
    }

    @RequestMapping("/userList")
    @ResponseBody
    public String userList(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Consts.userList.size(); i++) {
            sb.append(Consts.userList.get(i));
            sb.append(",");
        }
        String s = sb.toString();
        template.convertAndSend("/topic/userList",s.substring(0,  s.length() -1));
        return "success";
    }

    /**
     * 获取当前登录的用户信息
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String info = request.getSession().getAttribute("uname").toString();
        return "{\"info\":\""+info+"\"}";
    }

    /**
     * 客户端发送消息方法
     * @param message
     * @param username
     * @return
     */
    @RequestMapping("/chat")
    @ResponseBody
    public String chat(String message,String username){
        System.out.println("-------------------"+message+"---"+username);
        //convertAndSend("/user/'user3'/luban")
        template.convertAndSendToUser(username,"luban",message);
        return  "success";
    }
}
