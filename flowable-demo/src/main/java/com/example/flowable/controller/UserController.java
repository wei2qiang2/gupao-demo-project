package com.example.flowable.controller;

import com.example.flowable.entity.User;
import com.example.flowable.mapper.UserMapper;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weiqiang
 * @date 2019/12/22 22:40
 * @decription
 * @updateInformaion
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("/list")
    public List<User> list() {
        List<User> users = userMapper.selectList(null);
        return users;
    }

    @GetMapping("/deploy")
    public List<String> getProinstanceList() {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        ArrayList<String> strings = new ArrayList<>();
        for (Deployment d: deployments) {
            strings.add(d.getId());
            strings.add(d.getDeploymentTime().toString());
        }
        return strings;
    }

//    @Transactional
    @GetMapping("/update")
    public List<User> list2() {
        List<User> users = userMapper.selectList(null);
        User u = new User();
        u.setAge(12);
        u.setEmail("123");
        u.setId(12L);
        u.setName("魏强");
        userMapper.insert(u);

        int a = 1/0;

        User u1 = new User();
        u.setAge(12);
        u.setEmail("123");
        u.setId(1L);
        u.setName("魏强");
        userMapper.updateById(u1);
        return users;
    }
}
