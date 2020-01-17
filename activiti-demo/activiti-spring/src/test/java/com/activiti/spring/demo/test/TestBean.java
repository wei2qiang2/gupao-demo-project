package com.activiti.spring.demo.test;

import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-spring.mappers"})
public class TestBean {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void getRepositoryService(){
        System.out.println("repositoryService:" + repositoryService.toString());
    }
}
