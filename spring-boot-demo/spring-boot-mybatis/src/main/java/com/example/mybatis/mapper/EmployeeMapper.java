package com.example.mybatis.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.mybatis.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper    extends BaseMapper<Employee> {

}
