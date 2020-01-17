package com.example.flowable.config;

/**
 * @author weiqiang
 * @date 2019/12/22 22:26
 * @decription
 * @updateInformaion
 */
import java.io.IOException;
import java.util.List;
import javax.sql.DataSource;
import org.flowable.app.spring.SpringAppEngineConfiguration;
import org.flowable.common.engine.impl.cfg.IdGenerator;
import org.flowable.common.engine.impl.persistence.StrongUuidGenerator;
import org.flowable.engine.configurator.ProcessEngineConfigurator;
import org.flowable.engine.spring.configurator.SpringProcessEngineConfigurator;
import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.*;
import org.flowable.spring.boot.app.AppEngineAutoConfiguration;
import org.flowable.spring.boot.app.AppEngineServicesAutoConfiguration;
import org.flowable.spring.boot.app.FlowableAppProperties;
import org.flowable.spring.boot.condition.ConditionalOnProcessEngine;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.process.FlowableProcessProperties;
import org.flowable.spring.boot.process.Process;
import org.flowable.spring.boot.process.ProcessAsync;
import org.flowable.spring.boot.process.ProcessAsyncHistory;
import org.flowable.spring.job.service.SpringAsyncExecutor;
import org.flowable.spring.job.service.SpringAsyncHistoryExecutor;
import org.flowable.spring.job.service.SpringRejectedJobsHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnProcessEngine
@EnableConfigurationProperties({FlowableProperties.class, FlowableMailProperties.class, FlowableHttpProperties.class, FlowableProcessProperties.class, FlowableAppProperties.class, FlowableIdmProperties.class})
@AutoConfigureAfter(
        value = {FlowableJpaAutoConfiguration.class, AppEngineAutoConfiguration.class},
        name = {"org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration"}
)
@AutoConfigureBefore({AppEngineServicesAutoConfiguration.class})
@Import({FlowableJobConfiguration.class})
public class ProcessEngineAutoConfiguration extends AbstractSpringEngineAutoConfiguration {
    protected final FlowableProcessProperties processProperties;
    protected final FlowableAppProperties appProperties;
    protected final FlowableIdmProperties idmProperties;
    protected final FlowableMailProperties mailProperties;
    protected final FlowableHttpProperties httpProperties;

    public ProcessEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableProcessProperties processProperties, FlowableAppProperties appProperties, FlowableIdmProperties idmProperties, FlowableMailProperties mailProperties, FlowableHttpProperties httpProperties) {
        super(flowableProperties);
        this.processProperties = processProperties;
        this.appProperties = appProperties;
        this.idmProperties = idmProperties;
        this.mailProperties = mailProperties;
        this.httpProperties = httpProperties;
    }

    @Bean
    @ProcessAsync
    @ConfigurationProperties(
            prefix = "flowable.process.async.executor"
    )
    @ConditionalOnMissingBean(
            name = {"processAsyncExecutor"}
    )
    public SpringAsyncExecutor processAsyncExecutor(ObjectProvider<TaskExecutor> taskExecutor, @Process ObjectProvider<TaskExecutor> processTaskExecutor, ObjectProvider<SpringRejectedJobsHandler> rejectedJobsHandler, @Process ObjectProvider<SpringRejectedJobsHandler> processRejectedJobsHandler) {
        return new SpringAsyncExecutor((TaskExecutor)this.getIfAvailable(processTaskExecutor, taskExecutor), (SpringRejectedJobsHandler)this.getIfAvailable(processRejectedJobsHandler, rejectedJobsHandler));
    }

    @Bean
    @ProcessAsyncHistory
    @ConfigurationProperties(
            prefix = "flowable.process.async-history.executor"
    )
    @ConditionalOnMissingBean(
            name = {"asyncHistoryExecutor"}
    )
    @ConditionalOnProperty(
            prefix = "flowable.process",
            name = {"async-history.enable"}
    )
    public SpringAsyncHistoryExecutor asyncHistoryExecutor(ObjectProvider<TaskExecutor> taskExecutor, @Process ObjectProvider<TaskExecutor> processTaskExecutor, ObjectProvider<SpringRejectedJobsHandler> rejectedJobsHandler, @Process ObjectProvider<SpringRejectedJobsHandler> processRejectedJobsHandler) {
        return new SpringAsyncHistoryExecutor((TaskExecutor)this.getIfAvailable(processTaskExecutor, taskExecutor), (SpringRejectedJobsHandler)this.getIfAvailable(processRejectedJobsHandler, rejectedJobsHandler));
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(@Qualifier("ds2DataSource")DataSource dataSource, PlatformTransactionManager platformTransactionManager, @Process ObjectProvider<IdGenerator> processIdGenerator, ObjectProvider<IdGenerator> globalIdGenerator, @ProcessAsync ObjectProvider<AsyncExecutor> asyncExecutorProvider, @ProcessAsyncHistory ObjectProvider<AsyncExecutor> asyncHistoryExecutorProvider) throws IOException {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        List<Resource> resources = this.discoverDeploymentResources(this.flowableProperties.getProcessDefinitionLocationPrefix(), this.flowableProperties.getProcessDefinitionLocationSuffixes(), this.flowableProperties.isCheckProcessDefinitions());
        if (resources != null && !resources.isEmpty()) {
            conf.setDeploymentResources((Resource[])resources.toArray(new Resource[0]));
            conf.setDeploymentName(this.flowableProperties.getDeploymentName());
        }

        AsyncExecutor springAsyncExecutor = (AsyncExecutor)asyncExecutorProvider.getIfUnique();
        if (springAsyncExecutor != null) {
            conf.setAsyncExecutor(springAsyncExecutor);
        }

        AsyncExecutor springAsyncHistoryExecutor = (AsyncExecutor)asyncHistoryExecutorProvider.getIfUnique();
        if (springAsyncHistoryExecutor != null) {
            conf.setAsyncHistoryEnabled(true);
            conf.setAsyncHistoryExecutor(springAsyncHistoryExecutor);
        }

        this.configureSpringEngine(conf, platformTransactionManager);
        this.configureEngine(conf, dataSource);
        conf.setDeploymentName(this.defaultText(this.flowableProperties.getDeploymentName(), conf.getDeploymentName()));
        conf.setDisableIdmEngine(!this.flowableProperties.isDbIdentityUsed() || !this.idmProperties.isEnabled());
        conf.setAsyncExecutorActivate(this.flowableProperties.isAsyncExecutorActivate());
        conf.setAsyncHistoryExecutorActivate(this.flowableProperties.isAsyncHistoryExecutorActivate());
        conf.setMailServerHost(this.mailProperties.getHost());
        conf.setMailServerPort(this.mailProperties.getPort());
        conf.setMailServerUsername(this.mailProperties.getUsername());
        conf.setMailServerPassword(this.mailProperties.getPassword());
        conf.setMailServerDefaultFrom(this.mailProperties.getDefaultFrom());
        conf.setMailServerForceTo(this.mailProperties.getForceTo());
        conf.setMailServerUseSSL(this.mailProperties.isUseSsl());
        conf.setMailServerUseTLS(this.mailProperties.isUseTls());
        conf.getHttpClientConfig().setUseSystemProperties(this.httpProperties.isUseSystemProperties());
        conf.getHttpClientConfig().setConnectionRequestTimeout(this.httpProperties.getConnectionRequestTimeout());
        conf.getHttpClientConfig().setConnectTimeout(this.httpProperties.getConnectTimeout());
        conf.getHttpClientConfig().setDisableCertVerify(this.httpProperties.isDisableCertVerify());
        conf.getHttpClientConfig().setRequestRetryLimit(this.httpProperties.getRequestRetryLimit());
        conf.getHttpClientConfig().setSocketTimeout(this.httpProperties.getSocketTimeout());
        conf.setEnableProcessDefinitionHistoryLevel(this.processProperties.isEnableProcessDefinitionHistoryLevel());
        conf.setProcessDefinitionCacheLimit(this.processProperties.getDefinitionCacheLimit());
        conf.setEnableSafeBpmnXml(this.processProperties.isEnableSafeXml());
        conf.setHistoryLevel(this.flowableProperties.getHistoryLevel());
        conf.setActivityFontName(this.flowableProperties.getActivityFontName());
        conf.setAnnotationFontName(this.flowableProperties.getAnnotationFontName());
        conf.setLabelFontName(this.flowableProperties.getLabelFontName());
        conf.setFormFieldValidationEnabled(this.flowableProperties.isFormFieldValidationEnabled());
        IdGenerator idGenerator = (IdGenerator)this.getIfAvailable(processIdGenerator, globalIdGenerator);
        if (idGenerator == null) {
            idGenerator = new StrongUuidGenerator();
        }

        conf.setIdGenerator((IdGenerator)idGenerator);
        return conf;
    }

    @Configuration
    @ConditionalOnBean(
            type = {"org.flowable.app.spring.SpringAppEngineConfiguration"}
    )
    public static class ProcessEngineAppConfiguration extends BaseEngineConfigurationWithConfigurers<SpringProcessEngineConfiguration> {
        public ProcessEngineAppConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
                name = {"processAppEngineConfigurationConfigurer"}
        )
        public EngineConfigurationConfigurer<SpringAppEngineConfiguration> processAppEngineConfigurationConfigurer(ProcessEngineConfigurator processEngineConfigurator) {
            return (appEngineConfiguration) -> {
                appEngineConfiguration.addConfigurator(processEngineConfigurator);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public ProcessEngineConfigurator processEngineConfigurator(SpringProcessEngineConfiguration processEngineConfiguration) {
            SpringProcessEngineConfigurator processEngineConfigurator = new SpringProcessEngineConfigurator();
            processEngineConfigurator.setProcessEngineConfiguration(processEngineConfiguration);
            processEngineConfiguration.setDisableIdmEngine(true);
            this.invokeConfigurers(processEngineConfiguration);
            return processEngineConfigurator;
        }
    }
}

