package com.example.flowable;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.idm.IdmEngineAutoConfiguration;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
//import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Random;

@SpringBootApplication(exclude = IdmEngineAutoConfiguration.class)//exclude = ProcessEngineAutoConfiguration.class
@MapperScan("com.example.flowable.mapper")
@EnableTransactionManagement
public class FlowableDemoApplication {

    public static void main(String[] args) {
//        SpringApplication application = new SpringApplicationBuilder().build(args);
//        application.run(args);
        SpringApplication.run(FlowableDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final TaskService taskService) {

        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
//                System.err.println("Number of processes definitions : "
//                        + repositoryService.createProcessDefinitionQuery().count());
//                System.err.println("Number of tasks : " + taskService.createTaskQuery().count());
//                    runtimeService.startProcessInstanceByKey("SGJHFLOW", "BUSSINESSKEY" + new Random().nextInt(1000));
//                System.err.println("Number of tasks after processes start: "
//                        + taskService.createTaskQuery().count());
                List<Task> list = taskService.createTaskQuery().taskAssignee("a").list();
                System.err.println(list.toString());
            }
        };
    }

}
