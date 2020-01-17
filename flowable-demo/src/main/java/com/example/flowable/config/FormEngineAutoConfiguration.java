package com.example.flowable.config;

/**
 * @author weiqiang
 * @date 2019/12/22 22:22
 * @decription
 * @updateInformaion
 */

import java.io.IOException;
import java.util.List;
import javax.sql.DataSource;
import org.flowable.app.spring.SpringAppEngineConfiguration;
import org.flowable.form.engine.configurator.FormEngineConfigurator;
import org.flowable.form.spring.SpringFormEngineConfiguration;
import org.flowable.form.spring.configurator.SpringFormEngineConfigurator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.AbstractSpringEngineAutoConfiguration;
import org.flowable.spring.boot.BaseEngineConfigurationWithConfigurers;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.ProcessEngineServicesAutoConfiguration;
import org.flowable.spring.boot.app.AppEngineAutoConfiguration;
import org.flowable.spring.boot.app.AppEngineServicesAutoConfiguration;
import org.flowable.spring.boot.condition.ConditionalOnFormEngine;
import org.flowable.spring.boot.form.FlowableFormProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnFormEngine
@EnableConfigurationProperties({FlowableProperties.class, FlowableFormProperties.class})
@AutoConfigureAfter({AppEngineAutoConfiguration.class, ProcessEngineAutoConfiguration.class})
@AutoConfigureBefore({AppEngineServicesAutoConfiguration.class, ProcessEngineServicesAutoConfiguration.class})
public class FormEngineAutoConfiguration extends AbstractSpringEngineAutoConfiguration {
    protected final FlowableFormProperties formProperties;

    public FormEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableFormProperties formProperties) {
        super(flowableProperties);
        this.formProperties = formProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringFormEngineConfiguration formEngineConfiguration(@Qualifier("ds2DataSource")DataSource dataSource, PlatformTransactionManager platformTransactionManager) throws IOException {
        SpringFormEngineConfiguration configuration = new SpringFormEngineConfiguration();
        List<Resource> resources = this.discoverDeploymentResources(this.formProperties.getResourceLocation(), this.formProperties.getResourceSuffixes(), this.formProperties.isDeployResources());
        if (resources != null && !resources.isEmpty()) {
            configuration.setDeploymentResources((Resource[])resources.toArray(new Resource[0]));
            configuration.setDeploymentName(this.formProperties.getDeploymentName());
        }

        this.configureSpringEngine(configuration, platformTransactionManager);
        this.configureEngine(configuration, dataSource);
        return configuration;
    }

    @Configuration
    @ConditionalOnBean(
        type = {"org.flowable.app.spring.SpringAppEngineConfiguration"}
    )
    public static class FormEngineAppEngineConfiguration extends BaseEngineConfigurationWithConfigurers<SpringFormEngineConfiguration> {
        public FormEngineAppEngineConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
            name = {"formAppEngineConfigurationConfigurer"}
        )
        public EngineConfigurationConfigurer<SpringAppEngineConfiguration> formAppEngineConfigurationConfigurer(FormEngineConfigurator formEngineConfigurator) {
            return (appEngineConfiguration) -> {
                appEngineConfiguration.addConfigurator(formEngineConfigurator);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public FormEngineConfigurator formEngineConfigurator(SpringFormEngineConfiguration configuration) {
            SpringFormEngineConfigurator formEngineConfigurator = new SpringFormEngineConfigurator();
            formEngineConfigurator.setFormEngineConfiguration(configuration);
            this.invokeConfigurers(configuration);
            return formEngineConfigurator;
        }
    }

    @Configuration
    @ConditionalOnBean(
        type = {"org.flowable.spring.SpringProcessEngineConfiguration"}
    )
    @ConditionalOnMissingBean(
        type = {"org.flowable.app.spring.SpringAppEngineConfiguration"}
    )
    public static class FormEngineProcessConfiguration extends BaseEngineConfigurationWithConfigurers<SpringFormEngineConfiguration> {
        public FormEngineProcessConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
            name = {"formProcessEngineConfigurationConfigurer"}
        )
        public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> formProcessEngineConfigurationConfigurer(FormEngineConfigurator formEngineConfigurator) {
            return (processEngineConfiguration) -> {
                processEngineConfiguration.addConfigurator(formEngineConfigurator);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public FormEngineConfigurator formEngineConfigurator(SpringFormEngineConfiguration configuration) {
            SpringFormEngineConfigurator formEngineConfigurator = new SpringFormEngineConfigurator();
            formEngineConfigurator.setFormEngineConfiguration(configuration);
            this.invokeConfigurers(configuration);
            return formEngineConfigurator;
        }
    }
}
