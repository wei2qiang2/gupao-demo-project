package com.example.flowable;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricDetail;
import org.flowable.engine.history.HistoricDetailQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ChangeActivityStateBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author weiqiang
 * @date 2019/12/21 22:03
 * @decription
 * @updateInformaion
 */
public class ProcessEngineTest {

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
                .addClasspathResource("SGJH8.bpmn")
                .addString("description", "施工计划测试")
                .addString("name", "测试计划")
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
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId("282501").list();
        list.forEach(item -> {
            System.err.println(item.getId());
            System.err.println(item.getKey());
        });
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("SGJHFLOW", "青岛运营");
//        System.err.println(processInstance.toString());
    }

    @Test
    public void startInstance() {
        Map<String, Object> map = new HashMap<>();
//        map.put("hasFirePlan", 1);
//        map.put("hasConditionDept", 0);
//        map.put("deptList", Arrays.asList("a", "b", "c"));
//        map.put("_FLOWABLE_SKIP_EXPRESSION_ENABLED", "true");
        map.put("toApproval", "张三"); // approvalPersonList设置提交审核人
//        ProcessInstance processInstance = ("SGJH-APPROAL-V2", map);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("SGJH-APPROAL-V2:6:282507", map);
        System.err.println(processInstance.getId());
    }

    @Test // 17501
    public void testGetProcessInstanceInfo() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceId("202501").list();
        for (int i = 0; i < list.size(); i++) {
            ProcessInstance p = list.get(i);
            System.err.println(p.toString());
        }
    }

    @Test
    public void testTaskList() {
        List<Task> list = taskService.createTaskQuery().processInstanceId("285001").list();
        for (int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            System.err.println("name:"+task.getName() + "-id:" + task.getId() +"-assign：" + task.getAssignee() + "-executionId:" + task.getExecutionId());
        }
    }

    @Test // 47508 提交审核  57504 60010 65005
    public void testTask() {
        Map<String, Object> map = new HashMap<>();
        map.put("hasFirePlan", 1);
        map.put("hasConditionDept", 0);
//        map.put("deptList", Arrays.asList("a", "b", "c"));
        taskService.complete("285007", map);
    }

    @Test // 47508 动火令审核 62505
    public void testFirePlan() {
        Map<String, Object> map = new HashMap<>();
        map.put("commit", 0);
        taskService.complete("290006", map);

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
//        runtimeService.createChangeActivityStateBuilder()
//                // executionId：180013
//                .moveExecutionToActivityId("217513", "TO_APPROVAL")
//                .changeState();
//
//        runtimeService.createChangeActivityStateBuilder()
//                // executionId：180013
//                .moveExecutionToActivityId("217514", "TO_APPROVAL")
//                .changeState();
//
//        runtimeService.createChangeActivityStateBuilder()
//                // executionId：180013
//                .moveExecutionToActivityId("217515", "TO_APPROVAL")
//                .changeState();
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("237501")
                .moveActivityIdTo("CONDITION_DEPT_APPROVAL","TO_APPROVAL")
                .changeState();
    }

    @Test
    public void testDeleteTask() {
        runtimeService.deleteMultiInstanceExecution("190008", false);
        runtimeService.deleteMultiInstanceExecution("190009", false);
//        taskService.deleteTasks(Arrays.asList("180026", "180031"));
    }
}
