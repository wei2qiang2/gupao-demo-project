package com.example.flowable;

import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author weiqiang
 * @date 2019/12/21 22:03
 * @decription
 * @updateInformaion
 */
public class SubProcessTest {

    private RepositoryService repositoryService = null;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private HistoryService historyService;
    private FormService formService;

    @Before
    public void before() {
        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcPassword("root");
        configuration.setJdbcUsername("root");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/aecc-flowable?useUnicode=true&useSSL=false&characterEncoding=UTF-8");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();

        this.repositoryService = processEngine.getRepositoryService();
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
//        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
        formService = processEngine.getFormService();
//        IdentityService identityService = processEngine.getIdentityService();
//        ManagementService managementService = processEngine.getManagementService();
        this.historyService = processEngine.getHistoryService();

    }

    @Test
    public void testDeploy() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("sub/SubProccessTest1.bpmn")
                .addString("description", "子流程测试版本1")
                .addString("name", "子流程测试")
                .addString("time", new Date().toString())
                .deploy();
        System.err.println(deploy.getId());
    }

    @Test
    public void testDeloyList() {
        List<Deployment> list = repositoryService.createDeploymentQuery().deploymentId("175001").list();
        list.forEach(i -> {
            System.err.println(i.getId());
        });
    }

    @Test
    public void testProDefiintion() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId("260001").list();
        list.forEach(item -> {
            System.err.println(item.getId());
            System.err.println(item.getKey());
        });
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("SGJHFLOW", "青岛运营");
//        System.err.println(processInstance.toString());
    }

    @Test
    public void startInstance() {
        Map<String, String> map = new HashMap<>();
        map.put("toApproval", "张三"); // approvalPersonList设置提交审核人
//        ProcessInstance processInstance = ("SGJH-APPROAL-V2", map);
        ProcessInstance processInstance = formService.submitStartFormData("SUB_PROCESS:1:260007", map);
        System.err.println(processInstance.getId());
    }

    @Test // 17501
    public void testGetProcessInstanceInfo() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceId("262501").list();
        for (int i = 0; i < list.size(); i++) {
            ProcessInstance p = list.get(i);
            System.err.println(p.toString());
        }
    }

    @Test
    public void testTaskList() {
        List<Task> list = taskService.createTaskQuery().processInstanceId("262501").list();
        for (int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            System.err.println("name\t\t"+ "id\t\t" + "assignee\t\t" +"executionId");
            System.err.println(task.getName()+ "\t\t" + task.getId() +"\t\t" + task.getAssignee() + "\t\t" + task.getExecutionId());
        }
    }

    @Test // 47508 提交审核  57504 60010 65005
    public void testTask() {

        taskService.complete("265013");
    }

    @Test // 47508 动火令审核 62505
    public void testFirePlan() {
        Map<String, Object> map = new HashMap<>();
        map.put("commit", 1);
        taskService.complete("245005", map);

    }

    @Test // 52517 52522 52527 配合部门审核
    public void testDept() {
        Map<String, Object> map = new HashMap<>();
        map.put("commit", 1);
        taskService.complete("252516", map);
        taskService.complete("252520", map);
        taskService.complete("252524", map);

    }

    @Test // 55006 施工计划审核不通过
    public void constractPlan() {
        Map<String, Object> map = new HashMap<>();
        map.put("commit", 1);
        taskService.complete("255006", map);

    }

    @Test
    public void testComplete() {
        Map<String, Object> map = new HashMap<>();
        map.put("commit", 0);
        taskService.complete("117505", map);
    }

    @Test // 查看历史任务
    public void testDeleteProDef() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId("107501").list();
        for (int i = 0; i < list.size(); i++) {
            System.err.println(list.get(i).getAssignee());
        }
    }

    @Test
    public void testHistory() {
//        List<HistoricDetail> list = historyService.createHistoricDetailQuery().activityInstanceId("17501").list();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId("222501").list();
//        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId("177501").list();
        for (HistoricTaskInstance historicDetail : list) {
            System.err.println(historicDetail.getId()+":" + historicDetail.getName() +":" + historicDetail.getEndTime() + ":executionId:" + historicDetail.getExecutionId());
            System.err.println();
        }
    }

    @Test
    public void testTurnBack() {
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("237501")
                .moveActivityIdTo("CONDITION_DEPT_APPROVAL","TO_APPROVAL")
                .changeState();
    }

    @Test
    public void testSubTaskInfo() {
        List<Task> list = taskService.createTaskQuery().processInstanceId("262501").list();
        list.forEach(l -> {
            System.err.println(l.getName() +"\t" + l.getId() +"\t" +l.getAssignee() +"\t" + l.getExecutionId() );
        });
    }
}
