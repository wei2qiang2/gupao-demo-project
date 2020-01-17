package com.activiti.spring.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupTaskTest {
    private Holiday holiday1 =
            new Holiday("1001,1002,1003","1004,1005,1006", "1007,1008", "renshi",3.1f, 1203232L, new Date(), new Date());


    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    private RuntimeService runtimeService = null;

    private RepositoryService repositoryService = null;

    private TaskService taskService = null;

    @Before
    public void beforeMethod(){
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
    }

    /**
     * 发布
     */
    @Test
    public void deploy(){
        Deployment deploy = repositoryService.createDeployment()
                .key("group-task-key")
                .name("组任务测试")
                .addClasspathResource("diagram/group-task.bpmn")
                .addClasspathResource("diagram/group-task.png")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryDefinition(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("group-task")
                .list();

        for (ProcessDefinition p: list) {
            System.out.println(p.getDeploymentId());
            System.out.println(p.getName());
            System.out.println(p.getId());
            System.out.println(p.getVersion());
            System.out.println(p.getCategory());
            System.out.println(p.getDescription());
            System.out.println(p.getEngineVersion());
            System.out.println(p.getDiagramResourceName());
            System.out.println(p.getResourceName());
            System.out.println(p.getTenantId());

            System.out.println("------------------------------------------------------------------------");
        }
    }

    /**
     * 启动实例
     */
    @Test
    public void startInstance(){

        Map<String, Object> varliables = new HashMap<>();
        varliables.put("holiday", holiday1);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("group-task", holiday1.getId().toString(), varliables);

        System.out.println(processInstance.getId());
        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getProcessDefinitionKey());
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessVariables().toString());
    }

    /**
     * 用户查询组任务
     */
    @Test
    public void queryGroupTask(){
         List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("1001")
                .processInstanceBusinessKey("1203232")
                .list();

        for (Task t: list) {
            System.out.println(t.getName());
            System.out.println(t.getAssignee());
            System.out.println(t.getId());
            System.out.println(t.getCreateTime());
        }
    }

    /**
     * 用户拾取任务
     * 说明：即使该用户不是候选人也能拾取，建议拾取时校验是否有资格 组任务拾取后，该任务已有负责人，通过候选人将查询不到该任
     */
    @Test
    public void claimTast(){

        taskService.claim("30008", "1001");

    }

    /**
     * 用户查询个人任务
     */
    @Test
    public void querySingleTask(){
         List<Task> list = taskService.createTaskQuery()
                .taskAssignee("1001")
                .processInstanceBusinessKey("1203232")
                .list();

        for (Task t: list) {
            System.out.println(t.getName());
            System.out.println(t.getAssignee());
            System.out.println(t.getId());
            System.out.println(t.getCreateTime());
        }
    }
}
