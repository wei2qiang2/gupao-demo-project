package com.example.flowable.config;

/**
 * @author weiqiang
 * @date 2019/12/22 22:19
 * @decription
 * @updateInformaion
 */
import java.io.IOException;
import java.util.List;
import javax.sql.DataSource;
import org.flowable.app.spring.SpringAppEngineConfiguration;
import org.flowable.common.engine.impl.persistence.StrongUuidGenerator;
import org.flowable.spring.boot.AbstractSpringEngineAutoConfiguration;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.app.FlowableAppProperties;
import org.flowable.spring.boot.condition.ConditionalOnAppEngine;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnAppEngine
@EnableConfigurationProperties({FlowableProperties.class, FlowableAppProperties.class, FlowableIdmProperties.class})
public class AppEngineAutoConfiguration extends AbstractSpringEngineAutoConfiguration {
    protected final FlowableAppProperties appProperties;
    protected final FlowableIdmProperties idmProperties;

    public AppEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableAppProperties appProperties, FlowableIdmProperties idmProperties) {
        super(flowableProperties);
        this.appProperties = appProperties;
        this.idmProperties = idmProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringAppEngineConfiguration springAppEngineConfiguration(@Qualifier("ds2DataSource") DataSource dataSource, PlatformTransactionManager platformTransactionManager) throws IOException {
        SpringAppEngineConfiguration conf = new SpringAppEngineConfiguration();
        List<Resource> resources = this.discoverDeploymentResources(this.appProperties.getResourceLocation(), this.appProperties.getResourceSuffixes(), this.appProperties.isDeployResources());
        if (resources != null && !resources.isEmpty()) {
            conf.setDeploymentResources((Resource[])resources.toArray(new Resource[0]));
        }

        this.configureSpringEngine(conf, platformTransactionManager);
        this.configureEngine(conf, dataSource);
        conf.setIdGenerator(new StrongUuidGenerator());
        return conf;
    }
}