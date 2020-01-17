package com.activiti.spring.demo;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo2Test {


    private ProcessEngine processEngine = null;
    private TaskService taskService = null;
    private RepositoryService repositoryService = null;
    private RuntimeService runtimeService = null;
    private HistoryService historyService = null;

    private Holiday less3Holiday =
            new Holiday("zhangsan","bumenjingli", "zongjingli", "renshi",2.5f, 1203232L, new Date(), new Date());


    private Holiday big3Holiday =
            new Holiday("zhangsan","bumenjingli", "zongjingli", "renshi",3.8f, 1203232L, new Date(), new Date());


    @Before
    public void testBeforeMethod(){
        processEngine = ProcessEngines.getDefaultProcessEngine();
        taskService = processEngine.getTaskService();
        runtimeService = processEngine.getRuntimeService();
        repositoryService = processEngine.getRepositoryService();
        historyService = processEngine.getHistoryService();
    }


    /**
     *发布holiday4流程
     */
    @Test
    public void testDeployHoliday4(){
        repositoryService.createDeployment()
                .addClasspathResource("diagram/holiday4.bpmn")
                .addClasspathResource("diagram/holiday4.png")
                .name("请假审批流程")
                .key("holidayProcess")
                .deploy();
    }

    /**
     * 查询发布的流程
     */
    @Test
    public void testQueryDeployProcessDefintion(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition: list){
            System.out.println(processDefinition.getId());
            System.out.println(processDefinition.getKey());
            System.out.println(processDefinition.getName());
            System.out.println(processDefinition.getDeploymentId());
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcessInstance(){

//
        Map<String, Object> varialible = new HashMap<>();
        //大于三天的
//        varialible.put("holiday", less3Holiday);
        varialible.put("holiday", big3Holiday);

        //startProcessInstanceByKey这儿的key使用的是流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1", less3Holiday.getId().toString(), varialible);

        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getDeploymentId());
        System.out.println(processInstance.getProcessDefinitionKey());
    }

    /**
     * 根据BussinessKey查询流程实例
     */
    @Test
    public void testQueryInstanceByBussinesskey(){

        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance processInstance: list){
            System.out.println(processInstance.getId());
            System.out.println(processInstance.getDeploymentId());
            System.out.println(processInstance.getBusinessKey());
        }
    }

    /**
     *查询任务
     *
     * 给任务查询对象指定流程实例的bussinesskey查询对应记录的流程任务
     */
    @Test
    public void testQueryTask(){
        List<Task> list = taskService.createTaskQuery().processInstanceBusinessKey("1203232").list();
        for (Task task: list){
            System.out.println(task.getId());
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    /**
     * 执行任务
     */
    @Test
    public void testExcuteTask(){
        //完成填写请假单
//        taskService.complete("2508");

        //完成部门经理审批
//        taskService.complete("5002");

        //完成人事存档
//        taskService.complete("7502");
    }

    /**
     * 执行请假大于三天任务
     */
    @Test
    public void testExcuteRantherTan3Task(){
        //完成填写请假单
//        taskService.complete("12508");

        //完成部门经理审批
//        taskService.complete("15002");

        //完成总经理审批
//        taskService.complete("17502");

        //完成人事存档
        taskService.complete("20002");
    }

    /**
     * 查询路程历史记录
     */
    @Test
    public void testQueryHistory(){

       //先查询processInstance
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey("1203232").list();

        for (int i = 0; i < list.size(); i++) {

            String processInstanId = list.get(i).getId();


            System.out.println(processInstanId);

            List<HistoricTaskInstance> historicDetails = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanId).list();
            for (HistoricTaskInstance detail: historicDetails) {
                System.out.println(detail.getName());
            }
        }
        
    }
}


