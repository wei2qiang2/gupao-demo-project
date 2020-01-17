# `SpringApplication`



`@springBootApplication`		标注功能

​	等同于 `@SpringBootConfiguration`

​		等同于	`@Configuration`

​			等同于 `@Component`

`SpringApplication	`		springboot 驱动spring应用上下文的引导类

```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    ...
}
```

## Component的派生性(注解不能继承)

​	被@Component标注的都会被@ComponentScan扫描到

​	处理类： ConfigurationClassParser

​	扫描类：	ClassPathBeanDifinitionScanner

​		父类：	ClassPathScanningCandidateComponentProvider

```java
protected void registerDefaultFilters() {
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }
```

Dubbo `@Service` 2.5.7  new AnnotationTypeFilter(Service.class)

### Spring注解编程模型

​	元注解：标注注解的注解（@Component是@Service 的元注解）

`@Component`

​	`@Service`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
 ...
}
```

​	`@Repository`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {
    ...
}
```

​	`@Controller`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    ...
}
```

​	`@Configuration`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
   ...
}
```

### spring模式注解：Stereotyoe Annotations

## Spring 注解驱动实例

注解驱动上下文：	`AnnotationConfigApplicationContext`

@ComponetScan`

​	spring framework 3.1 开始引入的（版本意识，不用springboot也剋实现自动装配）

- `@EnableAutiConfiguration	`	激活自动装配		@Enable模式（都hi@Enable开头的）

​	`@EnableMvc`

​	`@EnableTransactionManagerment`

​	`@EnableAspectJAutoProxy`

​	`@EnableAsync`

- `@SpringBootConfiguration` 相当于 `@Configuration`   (配置类的注解)

  ```java
  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Configuration
  public @interface SpringBootConfiguration {
  }
  ```

- 下面两者输出结果相同

```java
@Configuration
public class AnnotationDemo {

    public static void main(String[] args) {
        //找BeanDefinition
        // @Bean @Configuration
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        //注册配置类
        context.register(AnnotationDemo.class);
        //启动上下文
        context.refresh();
        System.out.println(context.getBean(AnnotationDemo.class));
    }
}
```



```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("server.port", 0);
        application.setDefaultProperties(map);
        //调整实例为非web程序
        application.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext context
                = application.run(args);
        System.out.println(Application.class);
        //输出应用上下文的类名	AnnotationConfigAppApplicationContext 等于				//@Configuration new 出来的
        System.out.println(context.getClass().getName());
    }

}
```

SpringApplication 的类型推断

​	SpringApplication 构造器 ->	deduceWebApplicationType() 

- deduceWebApplicationType()方法源码：

```java
static WebApplicationType deduceFromClasspath() {
        if (ClassUtils.isPresent("org.springframework.web.reactive.DispatcherHandler", (ClassLoader)null) && !ClassUtils.isPresent("org.springframework.web.servlet.DispatcherServlet", (ClassLoader)null) && !ClassUtils.isPresent("org.glassfish.jersey.servlet.ServletContainer", (ClassLoader)null)) {
            return REACTIVE;
        } else {
            String[] var0 = SERVLET_INDICATOR_CLASSES;
            int var1 = var0.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                String className = var0[var2];
                if (!ClassUtils.isPresent(className, (ClassLoader)null)) {
                    return NONE;
                }
            }

            return SERVLET;
        }
    }
```

`WebApplication.REACTIVE` 就是webflux

​	存在 DispacherHandler

​	存在spring-boot-starter-webflux

`WebApplication.NONE`	非web类型

​	spring-boot-starter-web和spring-boot-starter-webflux不存在

​	Servlet不存在

​	ConfigurableWebApplicationContext不存在

`WebApplication.SERVLET`	传统的SpringMVC	

​	spring-boot-starter-web

**jar包影响**

未调整正为非WEB应用的时候tomcat气筒了，推断为 WebApplicationType.SERVLET

**人工干预：**

调整为非Web应用的时候设置	WebApplicationType.NONE 

## SpringBoot事件

`ContextRefreshedEvent`

```java
protected void finishRefresh() {
        this.clearResourceCaches();
        this.initLifecycleProcessor();
        this.getLifecycleProcessor().onRefresh();
    	//发布事件
        this.publishEvent((ApplicationEvent)(new ContextRefreshedEvent(this)));
        LiveBeansView.registerApplicationContext(this);
    }
```

`refresh()` -> `finishRefresh() `- >` publishEvent(new ContextRefreshedEvent(this))` 

- ContextRefreshedEvent
  - ApplicationContextEvent
    - ApplicationEvent

`ContextCloseEvent`

`close() `-> `doClose() `-> `publishEvent(new ContextClosedEvent)`

- ContextClosedEvent
  - ApplicationContextEvent
    - ApplicationEvent

```java
protected void publishEvent(Object event, ResolvableType eventType) {
        Assert.notNull(event, "Event must not be null");
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Publishing event in " + this.getDisplayName() + ": " + event);
        }
		//Spring事件都是ApplicationEvent类型
        Object applicationEvent;
        if (event instanceof  ApplicationEvent) {
            applicationEvent = (ApplicationEvent)event;
        } else {
            applicationEvent = new PayloadApplicationEvent(this, event);
            if (eventType == null) {
                eventType = ((PayloadApplicationEvent)applicationEvent).getResolvableType();
            }
        }
		
        if (this.earlyApplicationEvents != null) {
            this.earlyApplicationEvents.add(applicationEvent);
        } else {
            //发送事件
            //实现类：SimpleApplicationEventMulticaster
       this.getApplicationEventMulticaster().multicastEvent((ApplicationEvent)applicationEvent, eventType);
        }

        if (this.parent != null) {
            if (this.parent instanceof AbstractApplicationContext) {
                ((AbstractApplicationContext)this.parent).publishEvent(event, eventType);
            } else {
                this.parent.publishEvent(event);
            }
        }

    }

    ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
        if (this.applicationEventMulticaster == null) {
            throw new IllegalStateException("ApplicationEventMulticaster not initialized - call 'refresh' before multicasting events via the context: " + this);
        } else {
            return this.applicationEventMulticaster;
        }
    }

    LifecycleProcessor getLifecycleProcessor() throws IllegalStateException {
        if (this.lifecycleProcessor == null) {
            throw new IllegalStateException("LifecycleProcessor not initialized - call 'refresh' before invoking lifecycle methods via the context: " + this);
        } else {
            return this.lifecycleProcessor;
        }
    }
```

Spring事件类型	`ApplicationEvent`

Spring事件监听器	`ApplicationListener`

Spring事件广播器	`ApplicationEventMulticaster`

Spring 事件理解为消息

- ​	`ApplicationEvent` 消息内容

- ​	`ApplicationListener		` 消息消费者

- ​	`ApplicationEventMulticaster	` 消息生产者

发送事件代码

```java
public void multicastEvent(final ApplicationEvent event, ResolvableType eventType) {
        ResolvableType type = eventType != null ? eventType : this.resolveDefaultEventType(event);
        Iterator var4 = this.getApplicationListeners(event, type).iterator();

        while(var4.hasNext()) {
            final ApplicationListener<?> listener = (ApplicationListener)var4.next();
            Executor executor = this.getTaskExecutor();
            if (executor != null) {
                executor.execute(new Runnable() {
                    public void run() {
                        SimpleApplicationEventMulticaster.this.invokeListener(listener, event);
                    }
                });
            } else {
                this.invokeListener(listener, event);
            }
        }

    }
```

实例

```java
public class SpringBootEvent {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootEvent.class)
                .listeners(applicationEvent -> {
                    System.out.println("监听到事件：" + applicationEvent.getClass());
                } )
                .run(args);
    }
}
```

运行结果：启动失败

```java
监听到事件：class org.springframework.boot.context.event.ApplicationContextInitializedEvent
监听到事件：class org.springframework.boot.context.event.ApplicationPreparedEvent
监听到事件：class org.springframework.boot.context.event.ApplicationFailedEvent
2019-06-25 06:55:43.522 ERROR 16676 --- [           main] o.s.boot.SpringApplication               : Application run failed
```

```java
@EnableAutoConfiguration
public class SpringBootEvent {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootEvent.class)
                .listeners(applicationEvent -> {
                    System.out.println("监听到事件：" + applicationEvent.getClass());
                } )
                .run(args);
    }
}
```

运行结果:启动成功

```java
监听到事件：class org.springframework.boot.context.event.ApplicationContextInitializedEvent
监听到事件：class org.springframework.boot.context.event.ApplicationPreparedEvent
监听到事件：class org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent
监听到事件：class org.springframework.boot.context.event.ApplicationStartedEvent
监听到事件：class org.springframework.boot.context.event.ApplicationReadyEvent
```

1. `ApplicationStartingEvent`1
2. `ApplicationEnvirmentPreparedEvent`                  2
3. `ApplicationPreparedEvent `             3
4. `ContextRefreshedEvent`
5. `ServletWebServerInitializedEvent`
6. `ApplicationStartedEvent`            4
7. `ApplicationReadyEvent `             6
8. `ContextClosedEvent`
9. `ApplicationFailedEvent`(失败的时候才会触发)              6

Spring Boot事件监听器

```properties
# Application Listeners
org.springframework.context.ApplicationListener=\
org.springframework.boot.ClearCachesApplicationListener,\
org.springframework.boot.builder.ParentContextCloserApplicationListener,\
org.springframework.boot.context.FileEncodingApplicationListener,\
org.springframework.boot.context.config.AnsiOutputApplicationListener,\
#监听ApplicationEnvirmentPreparedEvent分别读properties文件和yml文件
org.springframework.boot.context.config.ConfigFileApplicationListener,\
org.springframework.boot.context.config.DelegatingApplicationListener,\
org.springframework.boot.context.logging.ClasspathLoggingApplicationListener,\
org.springframework.boot.context.logging.LoggingApplicationListener,\
org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener
```

Spring Boot很多组件依赖于Spring boot事件监听器实现，本质也是Spring Framwork的事件监听机制

事件发布类

```java
package org.springframework.boot.context.event;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ErrorHandler;
public class EventPublishingRunListener implements SpringApplicationRunListener, Ordered {
    private final SpringApplication application;
    private final String[] args;
    private final SimpleApplicationEventMulticaster initialMulticaster;
    public EventPublishingRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
        //发布事件的发布类（生产者）
        this.initialMulticaster = new SimpleApplicationEventMulticaster();
        Iterator var3 = application.getListeners().iterator();

        while(var3.hasNext()) {
            ApplicationListener<?> listener = (ApplicationListener)var3.next();
            this.initialMulticaster.addApplicationListener(listener);
        }
    }
    public int getOrder() { return 0;}
    public void starting() {
        this.initialMulticaster.multicastEvent(new ApplicationStartingEvent(this.application, this.args));
    }
    public void environmentPrepared(ConfigurableEnvironment environment) {
        this.initialMulticaster.multicastEvent(new ApplicationEnvironmentPreparedEvent(this.application, this.args, environment));
    }
    public void contextPrepared(ConfigurableApplicationContext context) {
        this.initialMulticaster.multicastEvent(new ApplicationContextInitializedEvent(this.application, this.args, context));
    }
    public void contextLoaded(ConfigurableApplicationContext context) {
        ApplicationListener listener;
        for(Iterator var2 = this.application.getListeners().iterator(); var2.hasNext(); context.addApplicationListener(listener)) {
            listener = (ApplicationListener)var2.next();
            if (listener instanceof ApplicationContextAware) {
                ((ApplicationContextAware)listener).setApplicationContext(context);
            }
        }
        this.initialMulticaster.multicastEvent(new ApplicationPreparedEvent(this.application, this.args, context));
    }
    public void started(ConfigurableApplicationContext context) {
        context.publishEvent(new ApplicationStartedEvent(this.application, this.args, context));
    }
    public void running(ConfigurableApplicationContext context) {
        context.publishEvent(new ApplicationReadyEvent(this.application, this.args, context));
    }
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        ApplicationFailedEvent event = new ApplicationFailedEvent(this.application, this.args, context, exception);
        if (context != null && context.isActive()) {
            context.publishEvent(event);
        } else {
            if (context instanceof AbstractApplicationContext) {
                Iterator var4 = ((AbstractApplicationContext)context).getApplicationListeners().iterator();
                while(var4.hasNext()) {
                    ApplicationListener<?> listener = (ApplicationListener)var4.next();
                    this.initialMulticaster.addApplicationListener(listener);
                }
            }
            this.initialMulticaster.setErrorHandler(new EventPublishingRunListener.LoggingErrorHandler());
            this.initialMulticaster.multicastEvent(event);
        }
    }
    private static class LoggingErrorHandler implements ErrorHandler {
        private static Log logger = LogFactory.getLog(EventPublishingRunListener.class);
        private LoggingErrorHandler() {}
        public void handleError(Throwable throwable) {
            logger.warn("Error calling ApplicationEventListener", throwable);
        }
    }
}

```

`SpringApplication `利用

- `Spring`应用上下文（`ApplicationContext`）生命周期控制注解驱动
- `Spring`事件监听（`ApplicationEventMulticaster`）机制加载或者初始化组件