package com.demo.cache.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.demo.cache.entity.Employee;
import com.demo.cache.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "人员接口")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation(value = "人员分页")
    @GetMapping("/page-list")
    public ResponseEntity<Page<Employee>> getPageList(Page page){
        return ResponseEntity.ok(employeeService.getPageList(page));
    }

    @ApiOperation(value = "根据ID查询人员信息")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getPageList(@PathVariable("id")Integer id){
        return ResponseEntity.ok(employeeService.getInfoById(id));
    }

}
