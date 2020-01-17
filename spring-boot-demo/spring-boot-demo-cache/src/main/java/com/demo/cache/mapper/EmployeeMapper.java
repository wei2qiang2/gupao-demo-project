package com.demo.cache.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.demo.cache.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {


}
