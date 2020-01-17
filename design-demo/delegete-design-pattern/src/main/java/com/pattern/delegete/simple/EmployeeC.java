package com.pattern.delegete.simple;

/**
 * @author weiqiang
 * @date 2019/12/24 9:12
 * @decription
 * @updateInformaion
 */
public class EmployeeC implements IEmployee {
    public void ding() {
        System.err.println("员工C， 擅长设计， 正在设计业务架构....");
    }
}
