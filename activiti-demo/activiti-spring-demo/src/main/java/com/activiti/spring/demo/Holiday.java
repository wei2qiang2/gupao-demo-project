package com.activiti.spring.demo;

import java.io.Serializable;
import java.util.Date;

public class Holiday implements Serializable {

    /**
     * 填写请假单人员变量
     */
    private String assignee1;
    /**
     *部门审批人员变量
     */
    private String assignee2;
    /**
     * 总经理审批人员变量
     */
    private String assignee3;
    /**
     * 认识存档人员变量
     */
    private String assignee4;
    /**
     * 请假天数变量
     */
    private Float num;
    /**
     * 请假单ID
     */
    private Long id;
    /**
     * 开始请假时间
     */
    private Date startTime;
    /**
     * 结束请假时间
     */
    private Date endTime;


    public Holiday() {
    }

    public Holiday(String assignee1, String assignee2, String assignee3, String assignee4, Float num, Long id, Date startTime, Date endTime) {
        this.assignee1 = assignee1;
        this.assignee2 = assignee2;
        this.assignee3 = assignee3;
        this.assignee4 = assignee4;
        this.num = num;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getAssignee1() {
        return assignee1;
    }

    public void setAssignee1(String assignee1) {
        this.assignee1 = assignee1;
    }

    public String getAssignee2() {
        return assignee2;
    }

    public void setAssignee2(String assignee2) {
        this.assignee2 = assignee2;
    }

    public String getAssignee3() {
        return assignee3;
    }

    public void setAssignee3(String assignee3) {
        this.assignee3 = assignee3;
    }

    public String getAssignee4() {
        return assignee4;
    }

    public void setAssignee4(String assignee4) {
        this.assignee4 = assignee4;
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
