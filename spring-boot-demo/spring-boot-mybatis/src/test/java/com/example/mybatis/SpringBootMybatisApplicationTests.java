package com.example.mybatis;

import com.example.mybatis.entity.Employee;
import com.example.mybatis.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootMybatisApplicationTests {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testInsert() {

        //1151688362751492097
        Employee employee = new Employee();
//		employee.setdId(12);
        employee.setEmail("1432114216@qq.com");
        employee.setGender(2);
        employee.setLastName("张望");

        employeeMapper.insert(employee);
    }

    @Test
    public void query() {
        List<Employee> employees = employeeMapper.selectList(null);
        for (Employee e : employees) {
            System.out.println(e.getLastName());
        }

    }

}
