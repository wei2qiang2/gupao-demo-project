package com.example.flowable.config;

/**
 * @author weiqiang
 * @date 2019/12/22 22:21
 * @decription
 * @updateInformaion
 */

import java.io.IOException;
import java.util.List;
import javax.sql.DataSource;
import org.flowable.app.spring.SpringAppEngineConfiguration;
import org.flowable.cmmn.engine.configurator.CmmnEngineConfigurator;
import org.flowable.cmmn.spring.SpringCmmnEngineConfiguration;
import org.flowable.cmmn.spring.configurator.SpringCmmnEngineConfigurator;
import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.AbstractSpringEngineAutoConfiguration;
import org.flowable.spring.boot.BaseEngineConfigurationWithConfigurers;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.FlowableHttpProperties;
import org.flowable.spring.boot.FlowableJobConfiguration;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.ProcessEngineServicesAutoConfiguration;
import org.flowable.spring.boot.app.AppEngineAutoConfiguration;
import org.flowable.spring.boot.app.AppEngineServicesAutoConfiguration;
import org.flowable.spring.boot.app.FlowableAppProperties;
import org.flowable.spring.boot.cmmn.Cmmn;
import org.flowable.spring.boot.cmmn.FlowableCmmnProperties;
import org.flowable.spring.boot.condition.ConditionalOnCmmnEngine;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.job.service.SpringAsyncExecutor;
import org.flowable.spring.job.service.SpringRejectedJobsHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnCmmnEngine
@EnableConfigurationProperties({FlowableProperties.class, FlowableIdmProperties.class, FlowableCmmnProperties.class, FlowableAppProperties.class, FlowableHttpProperties.class})
@AutoConfigureAfter(
        value = {AppEngineAutoConfiguration.class, ProcessEngineAutoConfiguration.class},
        name = {"org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration"}
)
@AutoConfigureBefore({AppEngineServicesAutoConfiguration.class, ProcessEngineServicesAutoConfiguration.class})
@Import({FlowableJobConfiguration.class})
public class CmmnEngineAutoConfiguration extends AbstractSpringEngineAutoConfiguration {
    protected final FlowableCmmnProperties cmmnProperties;
    protected final FlowableIdmProperties idmProperties;
    protected final FlowableHttpProperties httpProperties;

    public CmmnEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableCmmnProperties cmmnProperties, FlowableIdmProperties idmProperties, FlowableHttpProperties httpProperties) {
        super(flowableProperties);
        this.cmmnProperties = cmmnProperties;
        this.idmProperties = idmProperties;
        this.httpProperties = httpProperties;
    }

    @Bean
    @Cmmn
    @ConfigurationProperties(
            prefix = "flowable.cmmn.async.executor"
    )
    @ConditionalOnMissingBean(
            name = {"cmmnAsyncExecutor"}
    )
    public SpringAsyncExecutor cmmnAsyncExecutor(ObjectProvider<TaskExecutor> taskExecutor, @Cmmn ObjectProvider<TaskExecutor> cmmnTaskExecutor, ObjectProvider<SpringRejectedJobsHandler> rejectedJobsHandler, @Cmmn ObjectProvider<SpringRejectedJobsHandler> cmmnRejectedJobsHandler) {
        return new SpringAsyncExecutor((TaskExecutor)this.getIfAvailable(cmmnTaskExecutor, taskExecutor), (SpringRejectedJobsHandler)this.getIfAvailable(cmmnRejectedJobsHandler, rejectedJobsHandler));
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringCmmnEngineConfiguration cmmnEngineConfiguration(@Qualifier("ds2DataSource")DataSource dataSource, PlatformTransactionManager platformTransactionManager, @Cmmn ObjectProvider<AsyncExecutor> asyncExecutorProvider) throws IOException {
        SpringCmmnEngineConfiguration configuration = new SpringCmmnEngineConfiguration();
        List<Resource> resources = this.discoverDeploymentResources(this.cmmnProperties.getResourceLocation(), this.cmmnProperties.getResourceSuffixes(), this.cmmnProperties.isDeployResources());
        if (resources != null && !resources.isEmpty()) {
            configuration.setDeploymentResources((Resource[])resources.toArray(new Resource[0]));
            configuration.setDeploymentName(this.cmmnProperties.getDeploymentName());
        }

        AsyncExecutor asyncExecutor = (AsyncExecutor)asyncExecutorProvider.getIfUnique();
        if (asyncExecutor != null) {
            configuration.setAsyncExecutor(asyncExecutor);
        }

        this.configureSpringEngine(configuration, platformTransactionManager);
        this.configureEngine(configuration, dataSource);
        configuration.setDeploymentName(this.defaultText(this.cmmnProperties.getDeploymentName(), configuration.getDeploymentName()));
        configuration.setDisableIdmEngine(!this.idmProperties.isEnabled());
        configuration.setAsyncExecutorActivate(this.flowableProperties.isAsyncExecutorActivate());
        configuration.getHttpClientConfig().setUseSystemProperties(this.httpProperties.isUseSystemProperties());
        configuration.getHttpClientConfig().setConnectionRequestTimeout(this.httpProperties.getConnectionRequestTimeout());
        configuration.getHttpClientConfig().setConnectTimeout(this.httpProperties.getConnectTimeout());
        configuration.getHttpClientConfig().setDisableCertVerify(this.httpProperties.isDisableCertVerify());
        configuration.getHttpClientConfig().setRequestRetryLimit(this.httpProperties.getRequestRetryLimit());
        configuration.getHttpClientConfig().setSocketTimeout(this.httpProperties.getSocketTimeout());
        configuration.setHistoryLevel(this.flowableProperties.getHistoryLevel());
        configuration.setEnableSafeCmmnXml(this.cmmnProperties.isEnableSafeXml());
        configuration.setFormFieldValidationEnabled(this.flowableProperties.isFormFieldValidationEnabled());
        return configuration;
    }

    @Configuration
    @ConditionalOnBean(
            type = {"org.flowable.app.spring.SpringAppEngineConfiguration"}
    )
    public static class CmmnEngineAppConfiguration extends BaseEngineConfigurationWithConfigurers<SpringCmmnEngineConfiguration> {
        public CmmnEngineAppConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
                name = {"cmmnAppEngineConfigurationConfigurer"}
        )
        public EngineConfigurationConfigurer<SpringAppEngineConfiguration> cmmnAppEngineConfigurationConfigurer(CmmnEngineConfigurator cmmnEngineConfigurator) {
            return (appEngineConfiguration) -> {
                appEngineConfiguration.addConfigurator(cmmnEngineConfigurator);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public CmmnEngineConfigurator cmmnEngineConfigurator(SpringCmmnEngineConfiguration cmmnEngineConfiguration) {
            SpringCmmnEngineConfigurator cmmnEngineConfigurator = new SpringCmmnEngineConfigurator();
            cmmnEngineConfigurator.setCmmnEngineConfiguration(cmmnEngineConfiguration);
            cmmnEngineConfiguration.setDisableIdmEngine(true);
            this.invokeConfigurers(cmmnEngineConfiguration);
            return cmmnEngineConfigurator;
        }
    }

    @Configuration
    @ConditionalOnBean(
            type = {"org.flowable.spring.SpringProcessEngineConfiguration"}
    )
    @ConditionalOnMissingBean(
            type = {"org.flowable.app.spring.SpringAppEngineConfiguration"}
    )
    public static class CmmnEngineProcessConfiguration extends BaseEngineConfigurationWithConfigurers<SpringCmmnEngineConfiguration> {
        public CmmnEngineProcessConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
                name = {"cmmnProcessEngineConfigurationConfigurer"}
        )
        public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> cmmnProcessEngineConfigurationConfigurer(CmmnEngineConfigurator cmmnEngineConfigurator) {
            return (processEngineConfiguration) -> {
                processEngineConfiguration.addConfigurator(cmmnEngineConfigurator);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public CmmnEngineConfigurator cmmnEngineConfigurator(SpringCmmnEngineConfiguration cmmnEngineConfiguration) {
            SpringCmmnEngineConfigurator cmmnEngineConfigurator = new SpringCmmnEngineConfigurator();
            cmmnEngineConfigurator.setCmmnEngineConfiguration(cmmnEngineConfiguration);
            cmmnEngineConfiguration.setDisableIdmEngine(true);
            this.invokeConfigurers(cmmnEngineConfiguration);
            return cmmnEngineConfigurator;
        }
    }
}
