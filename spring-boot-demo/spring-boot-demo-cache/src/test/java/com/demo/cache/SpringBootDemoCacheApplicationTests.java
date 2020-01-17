package com.demo.cache;

import com.demo.cache.entity.Employee;
import com.demo.cache.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDemoCacheApplicationTests {

	@Autowired
	private EmployeeMapper employeeMapper;

	@Test
	public void contextLoads() {
		Employee employee = new Employee(1, "张三", "1432114216@qq.com", 1, 12);
		employeeMapper.insert(employee);
	}

	@Test
	public void insert(){
		System.out.println("employeeMapper:" + employeeMapper);
	}
}
