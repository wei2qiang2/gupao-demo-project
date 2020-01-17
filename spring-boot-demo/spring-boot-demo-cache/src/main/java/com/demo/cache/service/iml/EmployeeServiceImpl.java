package com.demo.cache.service.iml;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.demo.cache.entity.Employee;
import com.demo.cache.mapper.EmployeeMapper;
import com.demo.cache.service.EmployeeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl   extends ServiceImpl<EmployeeMapper, Employee>  implements  EmployeeService{



    @Override
    public boolean insertOne(Employee employee) {
        return this.insert(employee);
    }

    @Override
    public boolean edit(Employee employee) {
        return this.updateById(employee);
    }

    @Override
    public boolean delete(List<Integer> ids) {
        return this.deleteBatchIds(ids);
    }

    @Override
    public boolean batchInsert(List<Employee> employeeList) {
        return this.insertBatch(employeeList);
    }


    @Cacheable(cacheNames = "emp" , key = "#id")
    @Override
    public Employee getInfoById(Integer id) {
        return this.selectById(id);
    }

    @Override
    public Employee getInfoOne(Employee employee) {
        return this.selectById(employee.getdId());
    }

    @Override
    public List<Employee> getInfoList(Employee employee) {
        EntityWrapper<Employee> wrapper = new EntityWrapper<>();
        wrapper.setEntity(employee);
        return this.selectList(wrapper);
    }

    @Cacheable(cacheNames = "employee.page",  key = "#page.getCurrent()")
    @Override
    public Page<Employee> getPageList(Page<Employee> page) {
        return this.selectPage(page);
    }
}
