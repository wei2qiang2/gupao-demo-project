//package com.example.flowable;
//
//import com.example.flowable.entity.User;
//import com.example.flowable.mapper.UserMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class FlowableDemoApplicationTests {
//
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Test
//    public void contextLoads() {
//    }
//
//
//    @Test
//    public void test1() {
//        List<User> userMappers = userMapper.selectList(null);
//        userMappers.forEach(item -> {
//            System.err.println(item.toString());
//        });
//        System.err.println(userMapper);
//    }
//}
//
