package com.demo.cache.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.demo.cache.entity.Employee;

import java.util.List;

public interface EmployeeService   extends IService<Employee>{

    boolean insertOne (Employee employee);

    boolean edit(Employee employee);

    boolean delete(List<Integer> ids);

    boolean batchInsert(List<Employee> employeeList);

    Employee getInfoById(Integer id);
    Employee getInfoOne(Employee employee);

    List<Employee> getInfoList(Employee employee);

    Page<Employee> getPageList(Page<Employee> page);
}
