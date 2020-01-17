package com.activiti.spring.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

public class DemoTest {

    private ProcessEngine processEngine = null;
    private RepositoryService repositoryService = null;


    /**
     * 创建ProcessEngine
     */
    @Before
    public void initProcessEngine(){
        this.processEngine = ProcessEngines.getDefaultProcessEngine();
        this.repositoryService = this.processEngine.getRepositoryService();
    }

    @Test
    public void testCreateTable() {
        ProcessEngineConfiguration configuration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.mappers");

        ProcessEngine processEngine = configuration.buildProcessEngine();

    }

    /**
     * 发布bpmn和png文件
     * 发布bpmn和png 的压缩zip文件
     * 最终数据库的保存都是分开保存的
     *
     * RepositoryService
     */
    @Test
    public void testDeployProcess() throws FileNotFoundException {
        //发布zip包
        InputStream in = new FileInputStream("");

        ZipInputStream zipInputStream = new ZipInputStream(in);


        //发布流程实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("diagram/holiday.bpmn")
                .addClasspathResource("diagram/holiday.png")
                .name("请假申请流程")
                .deploy();

        System.out.println("deploy:" + deploy.toString());

        //发布zip包的输入流
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream)
                .name("")
                .deploy();

    }

    /**
     * 启动一个流程实例
     *
     * RuntimeService
     */
    @Test
    public void testBeginProcess(){
        //启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");

        Map assigneeMap = new HashMap();

        assigneeMap.put("assigneee0", "zhangsan");
        assigneeMap.put("assigneee1", "bumenjingli");
        assigneeMap.put("assigneee2", "zongjingli");
        runtimeService.startProcessInstanceByKey("myProcess", "bussinesKye", assigneeMap);
        System.out.println("name: " + processInstance.getName());
        System.out.println("deployId:" +processInstance.getDeploymentId());
    }

    /**
     * 任务查询和任务完成
     *
     * TaskService
     */
    @Test
    public void testUserTask(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("5002");
        List<Task> tasksList = taskService.createTaskQuery().processDefinitionKey("myProcess").list();
        tasksList.forEach(System.out:: println);
    }

    /**
     * 查询流程定义的相关信息
     */
    @Test
    public void testProcessDefinitionInfo(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery.processDefinitionKey("myProcess");
        List<ProcessDefinition> list = processDefinitionQuery.list();
        list.forEach(System.out::println);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getId());
            System.out.println(list.get(i).getName());
            System.out.println(list.get(i).getKey());
            System.out.println(list.get(i).getVersion());
            System.out.println(list.get(i).toString());
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDefinition(){
        //查询部署ID
        String  deployId = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myProcess")
                .singleResult()
                .getId();
        //参数：流程部署ID
        repositoryService.deleteDeployment("1");
    }

    /**
     * 读物流程定义 的bpmn文件和png文件
     *
     * 场景：用户想查看流程的具体步骤要执行
     */
    @Test
    public void getPngAndBpmnFile() throws IOException {

        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinitionQuery myProcess = repositoryService.createProcessDefinitionQuery().processDefinitionKey("myProcess");

        String diagramResourceName = myProcess.singleResult().getDiagramResourceName();

        System.out.println("png: "+ diagramResourceName);

        String resourceName = myProcess.singleResult().getResourceName();

        System.out.println("resource: " + resourceName);

        String deploymentId = myProcess.singleResult().getDeploymentId();

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery().deploymentKey("myProcess");

        Deployment deployment = deploymentQuery.singleResult();

        InputStream pngInputStream = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);

        InputStream bmpnInputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);

        //这个地方的文件名为什么不能使用变量
        File file_png = new File("d:/purchasingflow01.png");

        OutputStream pngOutPutStream = new FileOutputStream(file_png);

        OutputStream bpmnOutPutStream = new FileOutputStream(new File("d:/a.bpmn"));

        IOUtils.copy(pngInputStream, pngOutPutStream);

        IOUtils.copy(bmpnInputStream, bpmnOutPutStream);

        pngInputStream.close();

        bmpnInputStream.close();

        pngOutPutStream.close();

        bpmnOutPutStream.close();
    }


}
