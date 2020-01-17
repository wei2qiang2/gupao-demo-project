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
import org.flowable.dmn.engine.configurator.DmnEngineConfigurator;
import org.flowable.dmn.spring.SpringDmnEngineConfiguration;
import org.flowable.dmn.spring.configurator.SpringDmnEngineConfigurator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.AbstractSpringEngineAutoConfiguration;
import org.flowable.spring.boot.BaseEngineConfigurationWithConfigurers;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.ProcessEngineServicesAutoConfiguration;
import org.flowable.spring.boot.app.AppEngineAutoConfiguration;
import org.flowable.spring.boot.app.AppEngineServicesAutoConfiguration;
import org.flowable.spring.boot.condition.ConditionalOnDmnEngine;
import org.flowable.spring.boot.dmn.FlowableDmnProperties;
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
@ConditionalOnDmnEngine
@EnableConfigurationProperties({FlowableProperties.class, FlowableDmnProperties.class})
@AutoConfigureAfter({AppEngineAutoConfiguration.class, ProcessEngineAutoConfiguration.class})
@AutoConfigureBefore({AppEngineServicesAutoConfiguration.class, ProcessEngineServicesAutoConfiguration.class})
public class DmnEngineAutoConfiguration extends AbstractSpringEngineAutoConfiguration {
    protected final FlowableDmnProperties dmnProperties;

    public DmnEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableDmnProperties dmnProperties) {
        super(flowableProperties);
        this.dmnProperties = dmnProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringDmnEngineConfiguration dmnEngineConfiguration(@Qualifier("ds2DataSource")DataSource dataSource, PlatformTransactionManager platformTransactionManager) throws IOException {
        SpringDmnEngineConfiguration configuration = new SpringDmnEngineConfiguration();
        List<Resource> resources = this.discoverDeploymentResources(this.dmnProperties.getResourceLocation(), this.dmnProperties.getResourceSuffixes(), this.dmnProperties.isDeployResources());
        if (resources != null && !resources.isEmpty()) {
            configuration.setDeploymentResources((Resource[])resources.toArray(new Resource[0]));
            configuration.setDeploymentName(this.dmnProperties.getDeploymentName());
        }

        this.configureSpringEngine(configuration, platformTransactionManager);
        this.configureEngine(configuration, dataSource);
        configuration.setHistoryEnabled(this.dmnProperties.isHistoryEnabled());
        configuration.setEnableSafeDmnXml(this.dmnProperties.isEnableSafeXml());
        configuration.setStrictMode(this.dmnProperties.isStrictMode());
        return configuration;
    }

    @Configuration
    @ConditionalOnBean(
            type = {"org.flowable.app.spring.SpringAppEngineConfiguration"}
    )
    public static class DmnEngineAppConfiguration extends BaseEngineConfigurationWithConfigurers<SpringDmnEngineConfiguration> {
        public DmnEngineAppConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
                name = {"dmnAppEngineConfigurationConfigurer"}
        )
        public EngineConfigurationConfigurer<SpringAppEngineConfiguration> dmnAppEngineConfigurationConfigurer(DmnEngineConfigurator dmnEngineConfigurator) {
            return (appEngineConfiguration) -> {
                appEngineConfiguration.addConfigurator(dmnEngineConfigurator);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public DmnEngineConfigurator dmnEngineConfigurator(SpringDmnEngineConfiguration configuration) {
            SpringDmnEngineConfigurator dmnEngineConfigurator = new SpringDmnEngineConfigurator();
            dmnEngineConfigurator.setDmnEngineConfiguration(configuration);
            this.invokeConfigurers(configuration);
            return dmnEngineConfigurator;
        }
    }

    @Configuration
    @ConditionalOnBean(
            type = {"org.flowable.spring.SpringProcessEngineConfiguration"}
    )
    @ConditionalOnMissingBean(
            type = {"org.flowable.app.spring.SpringAppEngineConfiguration"}
    )
    public static class DmnEngineProcessConfiguration extends BaseEngineConfigurationWithConfigurers<SpringDmnEngineConfiguration> {
        public DmnEngineProcessConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
                name = {"dmnProcessEngineConfigurationConfigurer"}
        )
        public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> dmnProcessEngineConfigurationConfigurer(DmnEngineConfigurator dmnEngineConfigurator) {
            return (processEngineConfiguration) -> {
                processEngineConfiguration.addConfigurator(dmnEngineConfigurator);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public DmnEngineConfigurator dmnEngineConfigurator(SpringDmnEngineConfiguration configuration) {
            SpringDmnEngineConfigurator dmnEngineConfigurator = new SpringDmnEngineConfigurator();
            dmnEngineConfigurator.setDmnEngineConfiguration(configuration);
            this.invokeConfigurers(configuration);
            return dmnEngineConfigurator;
        }
    }
}

